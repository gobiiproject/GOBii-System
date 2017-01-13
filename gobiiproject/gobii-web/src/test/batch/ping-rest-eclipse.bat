echo off
rem curl -i -H "Accept: application/json" -H "Content-Type: application/json"  -H "Cache-Control: no-cache, no-store, must-revalidate" -d "{\"name\":\"article\",\"scope\":\"dumb scope\"}" http://localhost:8080/gobii-web/resource/search/bycontenttype

echo *
echo *
echo *
echo *************************************************** PING TEST EXTRACT CONTROLLER
curl -i -H "Accept: application/json" -H "Content-Type: application/json"  -H "Cache-Control: no-cache, no-store, must-revalidate" -d "{\"pingRequests\":[\"Ping request from curl 1\",\"Ping request from curl 2\"],\"pingResponses\":[]}" http://localhost:8080/gobii-web/extract/ping

echo *
echo *
echo *
echo *************************************************** PING TEST LOAD CONTROLLER
curl -i -H "Accept: application/json" -H "Content-Type: application/json"  -H "Cache-Control: no-cache, no-store, must-revalidate" -d "{\"pingRequests\":[\"Ping request from curl 1\",\"Ping request from curl 2\"],\"pingResponses\":[]}" http://localhost:8080/gobii-web/load/ping

