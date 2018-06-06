killall java
rm -rf /apache-tomcat-7.0.41/logs/*
rm -rf /apache-tomcat-7.0.41/webapps/escritoriovirtualalabastrum/
rm -rf /apache-tomcat-7.0.41/webapps/ROOT/
rm /apache-tomcat-7.0.41/webapps/escritoriovirtualalabastrum.war
rm -rf /deployescritorio/*
cd /deployescritorio
git clone https://github.com/renandeandradevaz/escritoriovirtualalabastrum.git
cd /deployescritorio/escritoriovirtualalabastrum
ant
mv /deployescritorio/escritoriovirtualalabastrum/escritoriovirtualalabastrum.war /apache-tomcat-7.0.41/webapps/
bash /apache-tomcat-7.0.41/bin/startup.sh
