EtherDroid
==========

Etherpad Lite reader for Android, made by members of the FIXME Hackerspace in Lausanne, Switzerland. https://fixme.ch/wiki/

Features
--------
- Read pad

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
FIXME

WEBCLIENT
---------
    i=$(curl http://62.220.136.218:9001/socket.io/1/ --silent -b "token=t.Mo4J2EK2e9KJc51jai3K" | cut -d: -f 1)
    curl http://62.220.136.218:9001/socket.io/1/xhr-polling/$i -b token=t.Mo4J2EK2e9KJc51jai3K
