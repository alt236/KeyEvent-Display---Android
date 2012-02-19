/*******************************************************************************
 * Copyright 2012 Alexandros Schillings
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package aws.apps.keyeventdisplay;

import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import aws.apps.keyeventdisplay.containers.MyMainActivityState;
import aws.apps.keyeventdisplay.monitors.KernelLogMonitor;
import aws.apps.keyeventdisplay.monitors.LogCatMonitor;
import aws.apps.keyeventdisplay.util.UsefulBits;

public class MainActivity extends Activity {
	final String TAG = this.getClass().getName();
	private static final int MAX_TEXT_LINES = 1024;

	private TextView fldLog;
	private Button btnExport;
	private Button btnClear;
	private Button btnBreak;
	private CheckBox chkKeyEvents;
	private CheckBox chkKernel;
	private CheckBox chkLogcat;
	private ImageButton btnAbout;
	private ImageButton btnExit;
	private UsefulBits uB;
	private LogCatMonitor logCatM;
	private KernelLogMonitor kernelM;

	private final Handler logcatHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
			case LogCatMonitor.MSG_NEWLINE:
				addLogCatLine(msg.obj.toString());
				break;
			default:
				super.handleMessage(msg);
			}
		}
	};

	private final Handler kernelLogHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
			case LogCatMonitor.MSG_NEWLINE:
				addKernelLine(msg.obj.toString());
				break;
			default:
				super.handleMessage(msg);
			}
		}
	};

	public void addKernelLine(String text){
		if (!chkKernel.isChecked()){return;}
		displayLogLine("^ Kernel:       ", text, getResources().getColor(R.color.color_kernel));
	}

	public void addLogCatLine(String text){
		if (!chkLogcat.isChecked()){return;}
		displayLogLine("^ Logcat:       ", text, getResources().getColor(R.color.color_logcat));
	}

	private void displayKeyEvent(String title, KeyEvent event, int color) {
		sanityCheck(fldLog);

		StringBuilder sb = new StringBuilder();

		sb.append("<font color=" + color + ">" + title.replace(" ", "&nbsp;"));
		sb.append("action=" + event.getAction());
		sb.append(" code=" + event.getKeyCode());
		sb.append(" repeat=" + event.getRepeatCount());
		sb.append(" meta=" + event.getMetaState());
		sb.append(" scancode=" + event.getScanCode());
		sb.append(" mFlags=" + event.getFlags());
		sb.append(" label='" + event.getDisplayLabel() + "'");
		sb.append(" chars='" + event.getCharacters() + "'");
		sb.append(" number='" + event.getNumber() + "'");
		sb.append("</font> <br/>");

		fldLog.append(Html.fromHtml(sb.toString()));
		uB.autoScroll(fldLog);
	}

	private void displayLogLine(String title, String event, int color){
		StringBuilder sb = new StringBuilder();
		sb.append("<font color=" + color + ">" + title.replace(" ", "&nbsp;"));

		sb.append(event);

		sb.append("</font> <br/>");

		fldLog.append(Html.fromHtml(sb.toString()));
		uB.autoScroll(fldLog);
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "^ Intent Started");

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		uB = new UsefulBits(this);
		Object lastState = getLastNonConfigurationInstance();

		btnExport = (Button) findViewById(R.id.btnExport);
		btnClear = (Button) findViewById(R.id.btnClear);
		btnBreak = (Button) findViewById(R.id.btnBreak);
		btnAbout = (ImageButton) findViewById(R.id.btnAbout);
		btnExit = (ImageButton) findViewById(R.id.btnExit);

		chkKeyEvents = (CheckBox) findViewById(R.id.chkKeyEvents);
		chkKernel = (CheckBox) findViewById(R.id.chkKernel);
		chkLogcat = (CheckBox) findViewById(R.id.chkLogCat);

		if (fldLog == null) {
			fldLog = (TextView) findViewById(R.id.fldEvent);
			fldLog.setMovementMethod(new ScrollingMovementMethod());
		}

		if (!(lastState == null)) {

			fldLog.setText(((MyMainActivityState)lastState).getLogText());
			chkKernel.setChecked(((MyMainActivityState)lastState).isChkKenel());
			chkKeyEvents.setChecked(((MyMainActivityState)lastState).isChkKeyEvents());
			chkLogcat.setChecked(((MyMainActivityState)lastState).isChkLogcat());
			uB.autoScroll(fldLog);
		}

		btnAbout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				uB.showAboutDialogue();
			}
		});

		btnExit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				onStop();
				System.exit(0);
			}
		});

		btnClear.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				fldLog.setText(R.string.pressbuttons);
				fldLog.scrollTo(0, 0);
			}
		});

		btnBreak.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				fldLog.append(Html.fromHtml("<font color="+getResources().getColor(R.color.lime)+">" + getString(R.string.break_string)+"</font><br/>"));
			}
		});

		btnExport.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if(fldLog.getText().toString().length()>0 && (!fldLog.getText().toString().equals(getString(R.string.pressbuttons)))){
					String time = uB.formatDateTime("yyyy-MM-dd-HHmmssZ", new Date());
					uB.saveToFile("keyevent_"+ time +".txt", Environment.getExternalStorageDirectory(), fldLog.getText().toString());
				} else {
					uB.showToast("Nothing to save...", 
							Toast.LENGTH_SHORT, Gravity.TOP,0,0);					
				}

			}
		});	
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (!chkKeyEvents.isChecked()){return true;}
		displayKeyEvent("^ KeyDown:      ", event, getResources().getColor(R.color.color_keydown));
		return true;
	}

	public boolean onKeyLongPress(int keyCode, KeyEvent event) {
		if (!chkKeyEvents.isChecked()){return true;}
		displayKeyEvent("^ KeyLongPress: ", event, getResources().getColor(R.color.color_keylongpress));
		return true;
	}

	public boolean onKeyMultiple(int keyCode, int count, KeyEvent event) {
		if (!chkKeyEvents.isChecked()){return true;}
		displayKeyEvent("^ KeyMultiple:  ", event, getResources().getColor(R.color.color_keymultiple));
		return true;
	}

	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (!chkKeyEvents.isChecked()){return true;}
		displayKeyEvent("^ KeyUp:        ", event, getResources().getColor(R.color.color_keyup));
		return true;
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		MyMainActivityState state = new MyMainActivityState();
		state.setChkKenel(chkKernel.isChecked());
		state.setChkLogcat(chkLogcat.isChecked());
		state.setChkKeyEvents(chkKeyEvents.isChecked());

		state.setLogText(fldLog.getText());
		return (state);
	}

	@Override
	public void onStart()
	{
		Log.d(TAG, "^ onStart called");
		logCatM = new LogCatMonitor(getResources().getStringArray(R.array.logcat_filter))
		{
			@Override
			public void onError(final String msg, final Throwable e)
			{
				//super.onError(msg, e);
				runOnUiThread(new Runnable() {
					public void run()
					{
						Toast.makeText(getApplicationContext(), msg + ": " + e,
								Toast.LENGTH_LONG).show();
					}
				});
			}

			@Override
			public void onNewline(String line)
			{
				//super.onNewline(line);
				Message msg = logcatHandler.obtainMessage(LogCatMonitor.MSG_NEWLINE);
				msg.obj = line;
				logcatHandler.sendMessage(msg);
			}
		};

		kernelM = new KernelLogMonitor(getResources().getStringArray(R.array.kmsg_filter))
		{
			@Override
			public void onError(final String msg, final Throwable e)
			{
				//super.onError(msg, e);
				runOnUiThread(new Runnable() {
					public void run()
					{
						Toast.makeText(getApplicationContext(), msg + ": " + e,
								Toast.LENGTH_LONG).show();
					}
				});
			}
			
			@Override
			public void onNewline(String line)
			{
				Message msg = logcatHandler.obtainMessage(KernelLogMonitor.MSG_NEWLINE);
				msg.obj = line;
				kernelLogHandler.sendMessage(msg);
			}
		};

		logCatM.start();
		Log.d(TAG, "^ kernelFilter: " + kernelM.getFilterWords());
		kernelM.start();
		super.onStart();
	}


	//
	/////////////////////////////////////////////////////////////////
	//

	@Override
	public void onStop()
	{
		Log.d(TAG, "^ onStop called");
		logCatM.stopCatter();
		logCatM = null;

		kernelM.stopCatter();
		kernelM = null;
		super.onStop();
	}	


	private void sanityCheck(TextView tv) {
		if (tv.getLineCount() > MAX_TEXT_LINES) {
			tv.setText("");
			tv.scrollTo(0, 0);
		}
	}	
};
