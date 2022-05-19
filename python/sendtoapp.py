import paho.mqtt.client as mqttClient
import time
import sys
 
def on_connect(client, userdata, flags, rc):
    if rc == 0:
        print("Connected to broker")
        global Connected                #Use global variable
        Connected = True                #Signal connection 
        with open(sys.argv[1],"rb") as fd:
          bytes_ =fd.read()
          client.publish("cv",bytes_)

    else:
        print("Connection failed")
 
Connected = False   #global variable for the state of the connection
 
broker_address= "127.0.0.1"
port = 1883
user = "yourUser"
password = "yourPassword"
 
client = mqttClient.Client("Python")               #create new instance
#client.username_pw_set(user, password=password)    #set username and password
client.on_connect= on_connect                      #attach function to callback
client.connect(broker_address, port=port)          #connect to broker
 
client.loop_start()        #start the loop
 
while Connected != True:    #Wait for connection
    time.sleep(0.1)
