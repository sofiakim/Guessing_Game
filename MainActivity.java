package com.rhhs.sofia.guessinggame;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android.view.View.OnClickListener;

import java.util.Locale;
import android.speech.tts.TextToSpeech;

public class MainActivity extends Activity implements TextToSpeech.OnInitListener
{

    private Button guessButton;

    private EditText guessBox;
    private TextView messageText;

    private int secretNo;
    private int noOfGuesses;

    private boolean gameOver;
    // Keep track of a TextToSpeech object
    private TextToSpeech txtToSpch;

    @ Override
	protected void onCreate (Bundle savedInstanceState)
    {
	super.onCreate (savedInstanceState);
	setContentView (R.layout.activity_main);

	// To connect the Button in the layout with the Button in the code
	guessButton = (Button) findViewById (R.id.guessButton);
	 guessButton.setOnClickListener (new OnClickListener ()
	    {
		/** Responds to the clicking of the Button
		 * @param view the View that was clicked on
		 */
		public void onClick (View view)
		{
		    if (!gameOver)
			makeAGuess ();
		    else
			newGame ();
		}
	    }
	    );
	// To connect EditText and TextView in the layout with
	// the EditText and TextView in the code
	guessBox = (EditText) findViewById (R.id.guessBox);
	messageText = (TextView) findViewById (R.id.message);

	txtToSpch = new TextToSpeech (this, this);

	// To start the game
	newGame ();
    }


    /** Initializes the txtToSpch variable and say the original message
      * @param status the initialization status (assume it is OK)
      */
    public void onInit (int status)
    {
	// Set up the language and speak the initial start up message
	txtToSpch.setLanguage (Locale.CANADA);
	txtToSpch.speak ("Guess a number between 1 and 100",
		TextToSpeech.QUEUE_FLUSH, null);
    }


    /** To clean up the TextToSpeech object
      */
    protected void onDestroy ()
    {
	// Call the onDestroy in Activity and shut down the TextToSpeech object
	super.onDestroy ();
	txtToSpch.shutdown ();
    }


   

    /** Gets ready for a new game by displaying an opening message,
     * generating a random number and initializing the number of guesses.
     */
    private void newGame ()
    {
	//Display a message in the messageText TextView
	messageText.setText ("Guess a number between 1 and 100");

	// or, if you set up an @string value, you could use the following:
	// messageText.setText(getString(R.string.guessPrompt));

	// Generate a secret number and show the number to the user using
	// a "Toast". This will be helpful when checking your program.
	secretNo = (int) (Math.random () * 100) + 1;
	Toast.makeText (getApplicationContext (), "Secret Number is " +
		secretNo, Toast.LENGTH_LONG).show ();

	// Reset the number of guesses to start the game
	noOfGuesses = 0;

	// Set the initial value of gameOver to false
	gameOver = false;

	// Make the guessBox box visible
	guessBox.setVisibility (View.VISIBLE);
    }


    /** Process the next guess - count the guesses and give feedback
     */
    private void makeAGuess ()
    {
	// Read in the guess as a String (toString is needed since
	// getText returns an Editable object but we want a String)
	String guessStr = guessBox.getText ().toString ();

	// When we created guessBox we set the "InputType" property to
	// "Number", so guessStr should only contain the digits 0 to 9
	// However, the user could still enter a blank guess so we need
	// to include code to handle an empty guessStr
	int guess = 0;
	if (guessStr.length () > 0)
	    guess = Integer.parseInt (guessStr);
	else
	{
	    messageText.setText ("Please enter a guess");
	    txtToSpch.speak ("Please enter a guess",
		    TextToSpeech.QUEUE_FLUSH, null);
	    return;
	}
    // Keep track of the number of guesses
    noOfGuesses++;
	// Update the noOfGuesses and
	// show a message (use messageText.setText()) to indicate if the
	// guess was too high, too low or correct. If the guess was correct
	// you should also indicate how many guesses were taken.
	    // When the guess is high
	    if (guess > secretNo)
	    {
		messageText.setText ("The guess is too high");
		txtToSpch.speak ("The guess is too high",
			TextToSpeech.QUEUE_FLUSH, null);
	    }
	    // When the guess is low
	    else if (guess < secretNo)
	    {
		messageText.setText ("The guess is too low");
		txtToSpch.speak ("The guess is too low",
			TextToSpeech.QUEUE_FLUSH, null);
	    }


	    else
	    {
	// When setting the text you can build new Strings just like println.
	// For example:
	
	if (noOfGuesses > 1)
	{
		messageText.setText ("Congratulations " + guess + " is correct.\n" +
				"You got it in " + noOfGuesses + " guesses.");
	txtToSpch.speak ("Congratulations " + guess + " is correct.\n" +
		"You got it in " + noOfGuesses + " guesses",
		TextToSpeech.QUEUE_FLUSH, null);
	}
	else
	{
		txtToSpch.speak ("Congratulations " + guess + " is correct.\n" +
				"You got it in " + noOfGuesses + " guess",
				TextToSpeech.QUEUE_FLUSH, null);
		messageText.setText ("Congratulations " + guess + " is correct.\n" +
				"You got it in " + noOfGuesses + " guess.");
	}
		
	    
	// Also you may want to hide the guessBox box when the number is
	// guessed until they select new game. To hide the guess box use:
	guessBox.setVisibility (View.INVISIBLE);
	   

	// When the player guesses the secret number,
	// change gameOver to true
	gameOver = true;

	// Change a New Game button
	guessButton.setText ("New Game");
	    }

	// Clear out the old guess to get ready for the next guess
	guessBox.setText ("");
    }


    @ Override
	public boolean onCreateOptionsMenu (Menu menu)
    {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater ().inflate (R.menu.main, menu);
	return true;
    }
}
