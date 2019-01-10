-module(loginManager).

-export([create/0, create_account/3, login/3, logout/1]).

-type username() :: string().
-type password() :: string().
-type session_state() :: boolean().
-type role() :: string().
-type user_data() :: {password(), session_state(), role()}.
-type state() :: #{username() => user_data()}.

create() ->
  register(?MODULE, spawn(fun() -> manager(#{}) end)).

request(Request) ->
  ?MODULE ! {Request, self()},
  receive
    Res ->
      Res
  end.

create_account(Username, Password, Role) ->
  request({create, Username, Password, Role}).
login(Username, Password, Role) ->
  request({login, Username, Password, Role}).
logout(Username) ->
  request({logout, Username}).

-spec manager(state()) -> ok.
manager(State) ->
  receive
    {{create, Username, Password, Role}, From} ->
      case maps:find(Username, State) of
        error ->
          From ! ok,
          manager(maps:put(Username, {Password, false, Role}, State));
        _ ->
          From ! user_exists,
          manager(State)
      end;
    {{login, Username, Password, Role}, From} ->
      io:fwrite('Searching username. ~p ~p\n', [Username, Role]),
      case maps:find(Username, State) of
        {ok, {P, false, R}} ->
          io:fwrite('Found username.\n'),
          io:fwrite('Checking Role\n'),
          case R of
            Role ->
              io:fwrite('Role ok.\n'),
              io:fwrite('Checking password. ~p\n', [Password]),
              case P of
                Password ->
                  io:fwrite('Password ok.\n'),
                  From ! ok,
                  manager(maps:put(Username, {P, true, Role}, State));
                _ ->
                  io:fwrite('Wrong password.\n'),
                  From ! invalid,
                  manager(State)
              end;
            _ ->
              io:fwrite('Role not ok.\n'),
              From ! invalid,
              manager(State)
          end;
        {ok, {P, true, R}} ->
          io:fwrite('Found username.\n'),
          io:fwrite('Checking password. ~p\n', [Password]),
          case P of
            Password ->
              io:fwrite('Password ok.\n'),
              io:fwrite('Already logged in.\n'),
              From ! invalid,
              manager(maps:put(Username, {P, true, Role}, State));
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
        {ok, {P, true, Role}} ->
          io:fwrite('Found username.\n'),
          From ! ok,
          manager(maps:put(Username, {P, false, Role}, State));
        error ->
          io:fwrite('Username not found.\n'),
          From ! invalid,
          manager(State)
      end
  end.
