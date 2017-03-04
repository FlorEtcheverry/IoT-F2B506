package appcomponent;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class Social {
	
	private String consumerKeyStr;
	private String consumerSecretStr;
	private String accessTokenStr;
	private String accessTokenSecretStr;
	
	private static int moreMaxErrorCode = 186;
	private static int maxChars = 140;

	public Social() {
	}
	
	public void setAuthValues(String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret) {
		consumerKeyStr = consumerKey;
		consumerSecretStr = consumerSecret;
		accessTokenStr = accessToken;
		accessTokenSecretStr = accessTokenSecret;
	}
	
	public boolean sendMedicationTookOnTime(String medName,int currentPoints) {
		return sendTweet("I have taken my medication ("+medName+") within 30 minutes. Now I have "+currentPoints+" points.");
	}

	public boolean sendMedicationTookOutOfTime(String medName, int currentPoints) {
		return sendTweet("I have taken my medication ("+medName+") out of time (+/-30min). Now I have "+currentPoints+" points.");
	}
	
	public boolean sendMedicationTookDuplicated(String medName, int currentPoints) {
		return sendTweet("I have taken my medication ("+medName+") duplicated. Now I have "+currentPoints+" points.");
	}
	
	public boolean sendMedicationNonTaken(String medName, int currentPoints) {
		return sendTweet("I have not taken my medication ("+medName+"). Now I have "+currentPoints+" points.");
	}

	public boolean sendMedicationFinished(String medName) {
		return sendTweet("My medication ("+medName+") has finnished. Needs replacement.");
	}
	
	private boolean sendTweet(String message) {
		try {
			Twitter twitter = new TwitterFactory().getInstance();

			twitter.setOAuthConsumer(consumerKeyStr, consumerSecretStr);
			AccessToken accessToken = new AccessToken(accessTokenStr,
					accessTokenSecretStr);

			twitter.setOAuthAccessToken(accessToken);
			twitter.updateStatus(message);
			return true;

		} catch (TwitterException te) {
			if (te.getErrorCode()==moreMaxErrorCode) {
				sendTweet(message.substring(0, maxChars));
			}
			System.err.println("Couldn't send Twitter update. "+te.getMessage());
			te.printStackTrace();
			return false;
		}
	}
}
