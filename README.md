EtherDroid
==========

Etherpad Lite reader for Android, made by members of the FIXME Hackerspace in Lausanne, Switzerland. https://fixme.ch/wiki/

Features
--------
- Read pad

Intents
-------
- Pad reader:
  Action: android.intent.action.VIEW
  Data: pad://host:port/apikey/PadID

TODO
----
- Real-time updates
- Edit pad

Participants
------------
- Gcmalloc
- Bad_Child
- Marc
- Undert
- Rorist

License
-------
- Icons are Apache 2.0 license from the "Icon Template Pack"

WEBCLIENT
---------
    i=$(curl http://62.220.136.218:9001/socket.io/1/ --silent -b "token=t.Mo4J2EK2e9KJc51jai3K" | cut -d: -f 1)
    curl http://62.220.136.218:9001/socket.io/1/xhr-polling/$i -b token=t.Mo4J2EK2e9KJc51jai3K
