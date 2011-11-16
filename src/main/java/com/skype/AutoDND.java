package com.skype;

import com.skype.Call;
import com.skype.CallAdapter;
import com.skype.CallStatusChangedListener;
import com.skype.Profile.Status;
import com.skype.Skype;
import com.skype.SkypeException;

public class AutoDND {
	public static void main(String[] args) throws SkypeException, InterruptedException {
		new AutoDND();
	}

	public AutoDND() throws SkypeException, InterruptedException {
		Skype.addCallListener(new CallAdapter() {
			@Override
			public void callReceived(Call call) throws SkypeException {
				AutoDND.startCall(call);
			}

			@Override
			public void callMaked(Call call) throws SkypeException {
				AutoDND.startCall(call);
			}
		});
		
		System.out.println("Connected.");
		
		while (Skype.isRunning()) {
			Thread.sleep(300);
		}
		
		System.out.println("Disconnected.");
	}

	private static void startCall(final Call call) throws SkypeException {
		final Status previous = Skype.getProfile().getStatus();

		if (Status.LOGGEDOUT.equals(previous)
				|| Status.INVISIBLE.equals(previous)) {
			return;
		}

		Skype.getProfile().setStatus(Status.DND);

		call.addCallStatusChangedListener(new CallStatusChangedListener() {
			public void statusChanged(com.skype.Call.Status status)
					throws SkypeException {
				switch (status) {
				case UNPLACED:
				case MISSED:
				case CANCELLED:
				case FAILED:
				case FINISHED:
					Skype.getProfile().setStatus(previous);
					break;
				}
			}
		});
	}
}
