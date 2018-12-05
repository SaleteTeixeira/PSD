#!/bin/bash

trap 'kill $(jobs -p)' EXIT

ERL='/usr/lib/erlang/bin/erl -pa ./erlang/out/production/erlang -pa ./erlang -eval server:main\(\). -s init stop -noshell &'

ant -f ./client -Dnb.internal.action.name=rebuild clean jar
/usr/lib/erlang/bin/erlc -pa ./erlang/out/production/erlang -pa ./erlang +debug_info ./erlang/src/*.erl

eval $ERL
java -jar ./client/dist/client.jar
