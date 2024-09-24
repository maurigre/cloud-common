#!/bin/sh

# Trigger release action on new release

if [ -z "$1" ]; then
    echo "Please provide a version number."
    echo "Usages: $0 [X.Y.Z]"
    exit 1
fi

version=$1

git tag -a "v$version" -m "Release version $version"
git push origin "v$version"
