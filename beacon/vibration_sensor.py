# test BLE Scanning software
# jcs 6/8/2014

import blescan
import sys

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

while True:
	returnedList = blescan.parse_events(sock, 1)
	print "----------"
	for beacon in returnedList:
		beacon_data = beacon.split(',')
		_uuid = beacon_data[1]
		_major = beacon_data[2]
		_minor = beacon_data[3]
		_tx_power = beacon_data[4]
		if _uuid == '9c374cdc3f8248f6a4f2a52a3e80581b':
                    print 'success'
		

