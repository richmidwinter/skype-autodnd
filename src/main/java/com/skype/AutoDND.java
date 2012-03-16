package com.skype;

import com.skype.Call;
import com.skype.CallAdapter;
import com.skype.CallStatusChangedListener;
import com.skype.Profile.Status;
import com.skype.Skype;
import com.skype.SkypeException;

public class AutoDND {

    private static final int THREAD_WAIT_MS = 300;

    private static boolean connected = false;

	public static void main(String[] args) throws SkypeException, InterruptedException {
		new AutoDND();
	}

	public AutoDND() throws SkypeException, InterruptedException {
		while (true) {
            if (connected != Skype.isRunning()) {
                connected = !connected;
                System.out.println(connected ? "Connected." : "Disconnected");

                if (connected) {
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
                }
            }
            
			Thread.sleep(THREAD_WAIT_MS);
		}
	}

	private static void startCall(final Call call) throws SkypeException {
		System.out.println("Call started.");
		
		final Status previous = Skype.getProfile().getStatus();

		if (Status.LOGGEDOUT.equals(previous)
				|| Status.INVISIBLE.equals(previous)) {
			return;
		}

		Skype.getProfile().setStatus(Status.DND);

		call.addCallStatusChangedListener(new CallStatusChangedListener() {
			public void statusChanged(com.skype.Call.Status status)
					throws SkypeException {
				System.out.println(String.format("Status changed to %s", status.toString()));
				
				switch (status) {
				case UNPLACED:
				case MISSED:
				case CANCELLED:
				case FAILED:
				case FINISHED:
					System.out.println("Call finished.");
					Skype.getProfile().setStatus(previous);
					break;
				}
			}
		});
	}
}
