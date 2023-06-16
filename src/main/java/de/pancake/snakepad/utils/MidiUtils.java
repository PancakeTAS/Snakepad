package de.pancake.snakepad.utils;

import java.util.List;

import com.harry1453.klaunchpad.api.Launchpad;
import com.harry1453.klaunchpad.api.MidiInputDevice;
import com.harry1453.klaunchpad.api.MidiInputDeviceInfo;
import com.harry1453.klaunchpad.api.MidiOutputDevice;
import com.harry1453.klaunchpad.api.MidiOutputDeviceInfo;

/**
 * Midi Utils
 */
public class MidiUtils {

	/**
	 * Try to create a midi launchpad
	 * @return Launchpad instance
	 */
	public static Launchpad setupLaunchpad() {
		var launchpad = Launchpad.connectToLaunchpadMK2((MidiInputDevice) Launchpad.Companion.openMidiInputDevice(findLaunchpadIn(), null), (MidiOutputDevice) Launchpad.Companion.openMidiOutputDevice(findLaunchpadOut(), null));
		launchpad.clearAllPadLights();
		return launchpad;
	}
	
	/**
	 * Try to find the launchpad midi output device
	 * @return Launchpad Midi Device or null
	 */
	public static MidiOutputDeviceInfo findLaunchpadOut() {
		@SuppressWarnings("unchecked")
		var m = (List<MidiOutputDeviceInfo>) Launchpad.Companion.listMidiOutputDevices(null);
		for (var midiOutputDeviceInfo : m)
			if (midiOutputDeviceInfo.getName().contains("Launchpad"))
				return midiOutputDeviceInfo;
		
		return null;
	}

	/**
	 * Try to find the launchpad midi input device
	 * @return Launchpad Midi Device or null
	 */
	public static MidiInputDeviceInfo findLaunchpadIn() {
		@SuppressWarnings("unchecked")
		var m = (List<MidiInputDeviceInfo>) Launchpad.Companion.listMidiInputDevices(null);
		for (var midiInputDeviceInfo : m)
			if (midiInputDeviceInfo.getName().contains("Launchpad"))
				return midiInputDeviceInfo;
		
		return null;
	}
	
}
