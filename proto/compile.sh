#!/usr/bin/env bash

protoc --java_out=../client/src/main/java/psd/client *.proto
protoc-erl *.proto
mv *.erl ../erlang/src/
mv *.hrl ../erlang/src/
