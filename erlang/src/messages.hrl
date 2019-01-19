%% -*- coding: utf-8 -*-
%% Automatically generated, do not edit
%% Generated by gpb_compile version 4.5.1

-ifndef(messages).
-define(messages, true).

-define(messages_gpb_version, "4.5.1").

-ifndef('REPLY_PB_H').
-define('REPLY_PB_H', true).
-record('Reply',
        {result = false         :: boolean() | 0 | 1 | undefined, % = 1
         message = []           :: iolist() | undefined % = 2
        }).
-endif.

-ifndef('LOGINREQUEST_PB_H').
-define('LOGINREQUEST_PB_H', true).
-record('LoginRequest',
        {username = []          :: iolist() | undefined, % = 1
         password = []          :: iolist() | undefined, % = 2
         role = []              :: iolist() | undefined % = 3
        }).
-endif.

-ifndef('REQUEST_PB_H').
-define('REQUEST_PB_H', true).
-record('Request',
        {type = []              :: iolist() | undefined % = 1
        }).
-endif.

-ifndef('LOGOUTREQUEST_PB_H').
-define('LOGOUTREQUEST_PB_H', true).
-record('LogoutRequest',
        {type = []              :: iolist() | undefined, % = 1
         username = []          :: iolist() | undefined % = 2
        }).
-endif.

-ifndef('AUCTIONBID_PB_H').
-define('AUCTIONBID_PB_H', true).
-record('AuctionBid',
        {type = []              :: iolist() | undefined, % = 1
         username = []          :: iolist() | undefined, % = 2
         company = []           :: iolist() | undefined, % = 3
         amount = 0             :: non_neg_integer() | undefined, % = 4, 32 bits
         interest = 0.0         :: float() | integer() | infinity | '-infinity' | nan | undefined % = 5
        }).
-endif.

-ifndef('FIXEDSUBSCRIPTION_PB_H').
-define('FIXEDSUBSCRIPTION_PB_H', true).
-record('FixedSubscription',
        {type = []              :: iolist() | undefined, % = 1
         username = []          :: iolist() | undefined, % = 2
         company = []           :: iolist() | undefined, % = 3
         amount = 0             :: non_neg_integer() | undefined % = 4, 32 bits
        }).
-endif.

-ifndef('AUCTION_PB_H').
-define('AUCTION_PB_H', true).
-record('Auction',
        {type = []              :: iolist() | undefined, % = 1
         company = []           :: iolist() | undefined, % = 2
         amount = 0             :: non_neg_integer() | undefined, % = 3, 32 bits
         interest = 0.0         :: float() | integer() | infinity | '-infinity' | nan | undefined % = 4
        }).
-endif.

-ifndef('FIXEDLOAN_PB_H').
-define('FIXEDLOAN_PB_H', true).
-record('FixedLoan',
        {type = []              :: iolist() | undefined, % = 1
         username = []          :: iolist() | undefined, % = 2
         amount = 0             :: non_neg_integer() | undefined, % = 3, 32 bits
         interest = 0.0         :: float() | integer() | infinity | '-infinity' | nan | undefined % = 4
        }).
-endif.

-ifndef('AUCTIONENTRY_PB_H').
-define('AUCTIONENTRY_PB_H', true).
-record('AuctionEntry',
        {type = []              :: iolist() | undefined, % = 1
         company = []           :: iolist() | undefined, % = 2
         amount = 0             :: non_neg_integer() | undefined, % = 3, 32 bits
         interest = 0.0         :: float() | integer() | infinity | '-infinity' | nan | undefined % = 4
        }).
-endif.

-ifndef('AUCTIONLIST_PB_H').
-define('AUCTIONLIST_PB_H', true).
-record('AuctionList',
        {type = []              :: iolist() | undefined, % = 1
         entry = []             :: [messages:'AuctionEntry'()] | undefined % = 2
        }).
-endif.

-ifndef('FIXEDENTRY_PB_H').
-define('FIXEDENTRY_PB_H', true).
-record('FixedEntry',
        {type = []              :: iolist() | undefined, % = 1
         company = []           :: iolist() | undefined, % = 2
         amount = 0             :: non_neg_integer() | undefined, % = 3, 32 bits
         interest = 0.0         :: float() | integer() | infinity | '-infinity' | nan | undefined % = 4
        }).
-endif.

-ifndef('FIXEDLIST_PB_H').
-define('FIXEDLIST_PB_H', true).
-record('FixedList',
        {type = []              :: iolist() | undefined, % = 1
         entry = []             :: [messages:'FixedEntry'()] | undefined % = 2
        }).
-endif.

-endif.
