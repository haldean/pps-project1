package isnork.g3;

import isnork.g3.SeaLifePrototypeBuilder;

import isnork.sim.SeaLife;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;

/**
 * En/decodes information about an observed creature into a series of 
 * one-character messages, ready to be transmitted by the iSnork.
 * Info included: species, ID, and location of the creature. 4 or 5 bits total.
 * 
 * Species: 1 bit       (26 values)
 * ID:      1 or 2 bits (26 or 676 values)
 * Loc:     2 bits      (676 values)
 */
public class WaterProofTranscoder implements Transcoder {
    public static int messageLength;
    private static boolean moreThan26PossiblePerSpecies;
    private HashBiMap<String,String> speciesMapping;
    private HashBiMap<String,String> IDMapping;
    private double scale;
    private double dim;

    public WaterProofTranscoder(
            SortedMap<Integer, String> speciesRanking, int sideLength) {
        speciesMapping = HashBiMap.create(26);
        int i=0;
        for (String speciesName : speciesRanking.values()) {
            speciesMapping.put(
                    speciesName, Character.toString((char) (i + 'a')));
            i++;
        }

        //TODO: get members-per-species info from Pokedex
        IDMapping = HashBiMap.create(26 * 26);
        messageLength = 5;
        moreThan26PossiblePerSpecies = true;

        scale = Math.min(26.0 / sideLength, 1);
        dim = (sideLength - 1) / 2.0;
    }

    @Override
    public int getMessageLength() {
        return messageLength;
    }

    @Override
    public List<String> encode(String name, int id, Point2D location) {
        return encode(name, String.valueOf(id), location);
    }

    @Override
    public List<String> encode(String name, String id, Point2D location) {
        StringBuilder messageBuilder = new StringBuilder();
        String mappedSpecies = getMappedSpecies(name);
        if(mappedSpecies == null) {
            //System.out.println(name+" is not a top species");
            return null;
        }
        messageBuilder.append(mappedSpecies);
        messageBuilder.append(getMappedID(id));
        messageBuilder.append(getMappedLocation(location));
        String completeMessage = messageBuilder.toString();

        List<String> messages = Lists.newArrayListWithCapacity(5);
        for(int i=0; i<5; i++) {
            messages.add(Character.toString(completeMessage.charAt(i)));
        }
        return messages;
    }

    @Override
    public SeaLife decode(String msg) {
        if(msg.length() != messageLength) {
            throw new IllegalStateException("The message to decode must be "
                +messageLength+" characters long.");
        }

        SeaLife recordedCreature = new SeaLife(new SeaLifePrototypeBuilder("foo").create());
        recordedCreature.setName(speciesMapping.inverse().get(Character.toString(msg.charAt(0))));

        String idSubString = msg.substring(1,3);
        if(!IDMapping.inverse().containsKey(idSubString)) {
            IDMapping.put(IDfromAlpha(idSubString), idSubString);
        }
        recordedCreature.setId(getUnmappedID(idSubString));

        recordedCreature.setLocation(getUnmappedLocation(msg.substring(3,5)));

        return recordedCreature;
    }

    private String getMappedSpecies(String species) {
        return speciesMapping.get(species);
    }

    private String getMappedID(String ID) {
        if(!IDMapping.containsKey(ID)) {
            //we haven't seen this one before
            IDMapping.put(ID, IDtoAlpha(IDMapping.size()));
        }
        return IDMapping.get(ID);
    }

    private int getUnmappedID(String encodedID) {
        if(!IDMapping.inverse().containsKey(encodedID)) {
            IDMapping.put(IDfromAlpha(encodedID), encodedID);
        }
        return Integer.parseInt(IDMapping.inverse().get(encodedID));
    }

    //this conversion is from 2010:g1
    private String getMappedLocation(Point2D location) {
        char x = (char)(scale * (location.getX() + dim) + 'a');
        char y = (char)(scale * (location.getY() + dim) + 'a');
        return Character.toString(x) + Character.toString(y);
    }

    //this conversion is from 2010:g1
    private Point2D getUnmappedLocation(String encodedLocation) {
        double x = Math.ceil((double)(encodedLocation.charAt(0) - 'a') / scale - dim);
        double y = Math.ceil((double)(encodedLocation.charAt(1) - 'a') / scale - dim);
        return new Point2D.Double(x, y);
    }

    private String IDtoAlpha(int num) {
        StringBuilder encodedAlpha = new StringBuilder();
        do {
            int rem = num % 26;
            encodedAlpha.insert(0, (char)(rem + 'a'));
            num = (num - rem) / 26;
        } while(num > 0);
        
        if(moreThan26PossiblePerSpecies) {
            //pad to two digits
            if(encodedAlpha.length() == 1) {
                encodedAlpha.insert(0, "a");
            }
        }
        
        return encodedAlpha.toString();
    }

    private String IDfromAlpha(String encodedID) {
        return String.valueOf((encodedID.charAt(0) - 'a') * 26 + (encodedID.charAt(1) - 'a'));
    }
}
