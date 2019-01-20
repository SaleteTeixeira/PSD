#!/usr/bin/env bash

protoc --java_out=../client/src/main/java/psd/client *.proto
protoc --java_out=../exchange/src/main/java/ *.proto
protoc-erl *.proto
mv *.erl ../erlang/src/
mv *.hrl ../erlang/src/
