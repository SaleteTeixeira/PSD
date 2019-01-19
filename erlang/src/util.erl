-module(util).

%% API
-export([number_of_exchanges/0, get_port/1, get_exchange/1]).

%% Implementation
number_of_exchanges() -> 1.

hash_string(String) -> hash_string(String, 0.0).
hash_string([], Acc) -> Acc;
hash_string([H | T], Acc) ->
  hash_string(T, Acc + (H + ((1.0 - rand:uniform()) * 100.0))).

get_port(Index) ->
  (round(Index) rem number_of_exchanges()) + 12345.

get_exchange(Company) ->
  get_port(hash_string(Company)).