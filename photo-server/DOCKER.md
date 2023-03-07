Quickstart: Using `colima` on MacOS M2
~~~
 brew install colima
 colima start
 colima status
 mkdir /Users/agilbert/.docker/run
 ln -s /Users/agilbert/.colima/default/docker.sock /Users/agilbert/.docker/run/docker.sock
~~~

Adapted from
https://alexos.dev/2022/01/02/docker-desktop-alternatives-for-m1-mac/