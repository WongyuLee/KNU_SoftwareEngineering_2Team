# test BLE Scanning software
# jcs 6/8/2014

from threading import Thread
import blescan
import sys
import time

import bluetooth._bluetooth as bluez

dev_id = 0
try:
        sock = bluez.hci_open_dev(dev_id)
        print "ble thread started"

except:
        print "error accessing bluetooth device..."
        sys.exit(1)

blescan.hci_le_set_scan_parameters(sock)
blescan.hci_enable_le_scan(sock)
global returnedList
global uuidList
returnedList=[]
uuidList=[]

def scan():
	while True:
		List = blescan.parse_events(sock, 5)
		for beacon in List:
			beacon_data = beacon.split(',')
			_uuid = beacon_data[1]
			_major = beacon_data[2]
			_minor = beacon_data[3]
			_tx_power = beacon_data[4]
			if _uuid in uuidList:
				continue
			else:
				returnedList.append(beacon)
				uuidList.append(_uuid)

if __name__=='__main__':
	sc = Thread(target = scan, args=())
	sc.start()
	while True:
		print "----------"
		for beacon in returnedList:
			beacon_data = beacon.split(',')
			_uuid = beacon_data[1]
			_major = beacon_data[2]
			_minor = beacon_data[3]
			_tx_power = beacon_data[4]
			print beacon
			if _uuid == '11111111111111111111111111111113':
				del returnedList[:]
				del uuidList[:]
