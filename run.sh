#!/bin/sh
set -e
mkdir -p out
find src -name '*.java' > out/sources.txt
javac -encoding UTF-8 -d out @out/sources.txt
java -Dstdout.encoding=UTF-8 -cp out br.unifor.trocatroca.Main
