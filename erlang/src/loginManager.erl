-module(loginManager).

-export([create/0, create_account/2, login/2, logout/1]).

-type username() :: string().
-type password() :: string().
-type session_state() :: boolean().
-type user_data() :: {password(), session_state()}.
-type state() :: #{username() => user_data()}.

create() ->
  register(?MODULE, spawn(fun() -> manager(#{}) end)).

request(Request) ->
  ?MODULE ! {Request, self()},
  receive
    Res ->
      Res
  end.

create_account(Username, Password) ->
  request({create, Username, Password}).
login(Username, Password) ->
  request({login, Username, Password}).
logout(Username) ->
  request({logout, Username}).

-spec manager(state()) -> ok.
manager(State) ->
  receive
    {{create, Username, Password}, From} ->
      case maps:find(Username, State) of
        error ->
          From ! ok,
          manager(maps:put(Username, {Password, false}, State));
        _ ->
          From ! user_exists,
          manager(State)
      end;

    {{login, Username, Password}, From} ->
      io:fwrite('Searching username. ~p\n', [Username]),
      case maps:find(Username, State) of
        {ok, {P, false}} ->
          io:fwrite('Found username.\n'),
          io:fwrite('Checking password. ~p\n', [Password]),
          case P of
            Password ->
              io:fwrite('Password ok.\n'),
              From ! ok,
              manager(maps:put(Username, {P, true}, State));
            _ ->
              io:fwrite('Wrong password.\n'),
              From ! invalid,
              manager(State)
          end;
        {ok, {P, true}} ->
          io:fwrite('Found username.\n'),
          io:fwrite('Checking password. ~p\n', [Password]),
          case P of
            Password ->
              io:fwrite('Password ok.\n'),
              io:fwrite('Already logged in.\n'),
              From ! invalid,
              manager(maps:put(Username, {P, true}, State));
            _ ->
              io:fwrite('Wrong password.\n'),
              From ! invalid,
              manager(State)
          end;
        error ->
          io:fwrite('Username not found.\n'),
          From ! invalid,
          manager(State)
      end;

    {{logout, Username}, From} ->
      io:fwrite('Searching username. ~p\n', [Username]),
      case maps:find(Username, State) of
        {ok, {P, true}} ->
          io:fwrite('Found username.\n'),
          From ! ok,
          manager(maps:put(Username, {P, false}, State));
        error ->
          io:fwrite('Username not found.\n'),
          From ! invalid,
          manager(State)
      end
  end.
