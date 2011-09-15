#!/bin/sh
echo "#!/bin/sh\n./runtests.sh" > .git/hooks/pre-commit
chmod +x .git/hooks/pre-commit
