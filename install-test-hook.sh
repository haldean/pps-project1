#!/bin/sh
echo '#!/bin/sh
./runtests.sh' > .git/hooks/pre-commit
chmod +x .git/hooks/pre-commit
