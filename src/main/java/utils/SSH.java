package utils;

import java.io.InputStream;
import java.util.Properties;

import org.testng.Assert;

import com.aventstack.extentreports.ExtentTest;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SSH 
{
	private Session session;
	private ExtentTest testStep;

	public SSH(String host, String user, String password, ExtentTest testStep)
	{
		try {
			this.testStep = testStep;
			Properties config = new Properties(); 
			config.put("StrictHostKeyChecking", "no");
			JSch jsch = new JSch();
			session = jsch.getSession(user, host, 22);
			session.setPassword(password);
			session.setConfig(config);
			session.connect();
			this.testStep.info("Connected to host ["+host+"] with user ["+user+"] and password ["+password+"]");
		} catch (JSchException e) {
			Assert.fail("Failed connecting to host ["+host+"] with user ["+user+"] and password ["+password+"]", e);
		}
	}

	public String executeCmd(String command)
	{
		StringBuilder str = new StringBuilder();
		try {
			Channel channel = session.openChannel("exec");
			((ChannelExec)channel).setCommand(command);
			channel.setInputStream(null);
			((ChannelExec)channel).setErrStream(System.err);
			InputStream in = channel.getInputStream();
			channel.connect();
			byte[] tmp = new byte[1024];
			int exitStatus = -1;
			while(true)
			{
				while(in.available()>0)
				{
					int i = in.read(tmp, 0, 1024);
					if(i<0)
						break;
					str.append(new String(tmp, 0, i));
				}
				if(channel.isClosed())
				{
					exitStatus = channel.getExitStatus();
					break;
				}
				Thread.sleep(50);
			}
			channel.disconnect();
			testStep.info("Command ["+command+"] executed with exit-status code ["+exitStatus+"]");
		} catch (Exception e) {
			Thread.currentThread().interrupt();
			Assert.fail("Failed to execute command ["+command+"]", e);
		}
		return str.toString();
	}

	public SSH endSession()
	{
		try {
			if(session!=null)
			{
				session.disconnect();
				testStep.info("SSH connection closed");
			}
		} catch (Exception e) {
			Assert.fail("Failed to close SSH connection", e);
		}
		return this;
	}
}