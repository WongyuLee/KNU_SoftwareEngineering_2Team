# test BLE Scanning software
# jcs 6/8/2014

import blescan
import sys
import RPi.GPIO as GPIO
import time

import bluetooth._bluetooth as bluez

GPIO.setmode(GPIO.BCM)
GPIO.setup(21, GPIO.OUT)

dev_id = 0
try:
	sock = bluez.hci_open_dev(dev_id)
	print "ble thread started"

except:
	print "error accessing bluetooth device..."
	sys.exit(1)

blescan.hci_le_set_scan_parameters(sock)
blescan.hci_enable_le_scan(sock)
returnedList=[]
uuidList=[]

while True:
	GPIO.output(21, True)
	print "off"
	List = blescan.parse_events(sock, 1)
        for beacon in List:
                beacon_data = beacon.split(',')
                _uuid = beacon_data[1]
                _major = beacon_data[2]
                _minor = beacon_data[3]
                _tx_power = beacon_data[4]
                if _uuid in uuidList:
                        continue
                else:
                        returnedList.extend(List)
                        uuidList.append(_uuid)

	for beacon in returnedList:
		beacon_data = beacon.split(',')
		_uuid = beacon_data[1]
		_major = beacon_data[2]
		_minor = beacon_data[3]
		_tx_power = beacon_data[4]
		if _uuid == '11111111111111111111111111111113':
			GPIO.output(21, False)
			print "on"
			time.sleep(5)
			GPIO.output(21,True)
			time.sleep(10)
