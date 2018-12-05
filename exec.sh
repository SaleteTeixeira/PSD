#!/bin/bash

trap 'kill $(jobs -p)' EXIT

ERL='/usr/lib/erlang/bin/erl -pa ./erlang/out/production/erlang -pa ./erlang -eval server:main\(\). -s init stop -noshell &'

ant -f ./client -Dnb.internal.action.name=rebuild clean jar

eval $ERL
java -jar ./client/dist/client.jar
