package com.projektgrupowy;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.googleglass.R;
import com.example.googleglass.R.id;
import com.google.android.glass.app.Card;
import com.google.android.glass.widget.CardBuilder;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;

public class MainActivity extends Activity {

	private List<CardBuilder> mCards;
	private CardScrollView mCardScrollView;
	private CustomCardScrollAdapter mAdapter;
	private GetFonts getFonts;
	int textSize = 14;
	int wordCount = 10;
	String textToView;
	TextView bodyText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		android.os.Debug.waitForDebugger(); //uncomment if you want to debbug in eclipse
		getFonts = new GetFonts(getApplicationContext());
		textToView = this.getString(R.string.sentence_1);
		createCards();
		mCardScrollView = new CardScrollView(this);
		mAdapter = new CustomCardScrollAdapter();
		mCardScrollView.setAdapter(mAdapter);
		mCardScrollView.activate();

		setContentView(mCardScrollView);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
			openOptionsMenu();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = new MenuInflater(getApplicationContext());
		inflater.inflate(R.menu.option_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.textsize_option_1:
			textSize = 14;
			mAdapter.notifyDataSetChanged();
			break;
		case R.id.textsize_option_2:
			textSize = 16;
			mAdapter.notifyDataSetChanged();
			break;
		case R.id.textsize_option_3:
			textSize = 18;
			mAdapter.notifyDataSetChanged();
			break;
		case R.id.textsize_option_4:
			textSize = 20;
			mAdapter.notifyDataSetChanged();
			break;
		case R.id.textsize_option_5:
			textSize = 22;
			mAdapter.notifyDataSetChanged();
			break;
		case R.id.wordsNumber_option_1:
			textToView = getResources().getString(R.string.sentence_1);
			wordCount = 10;
			mAdapter.notifyDataSetChanged();
			break;
		case R.id.wordsNumber_option_2:
			textToView = getResources().getString(R.string.sentence_2);
			wordCount = 20;
			mAdapter.notifyDataSetChanged();
			break;
		case R.id.wordsNumber_option_3:
			textToView = getResources().getString(R.string.sentence_3);
			wordCount = 30;
			mAdapter.notifyDataSetChanged();
			break;
		case R.id.wordsNumber_option_4:
			textToView = getResources().getString(R.string.sentence_4);
			wordCount = 40;
			mAdapter.notifyDataSetChanged();
			break;
		case R.id.wordsNumber_option_5:
			textToView = getResources().getString(R.string.sentence_5);
			wordCount = 50;
			mAdapter.notifyDataSetChanged();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void createCards() {
		mCards = new ArrayList<CardBuilder>();
		mCards.add(new CardBuilder(this, CardBuilder.Layout.EMBED_INSIDE).setEmbeddedLayout(R.layout.activity_main));
		mCards.add(new CardBuilder(this, CardBuilder.Layout.EMBED_INSIDE).setEmbeddedLayout(R.layout.activity_main));
		mCards.add(new CardBuilder(this, CardBuilder.Layout.EMBED_INSIDE).setEmbeddedLayout(R.layout.activity_main));
		mCards.add(new CardBuilder(this, CardBuilder.Layout.EMBED_INSIDE).setEmbeddedLayout(R.layout.activity_main));
	}

	private class CustomCardScrollAdapter extends CardScrollAdapter {
		@Override
		public int getPosition(Object item) {
			return mCards.indexOf(item);
		}

		@Override
		public int getCount() {
			return mCards.size();
		}

		@Override
		public Object getItem(int position) {
			return mCards.get(position);
		}

		@Override
		public int getViewTypeCount() {
			return CardBuilder.getViewTypeCount();
		}

		@Override
		public int getItemViewType(int position) {
			return mCards.get(position).getItemViewType();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = new CardBuilder(getApplicationContext(), CardBuilder.Layout.EMBED_INSIDE)
										.setEmbeddedLayout(R.layout.activity_main)
										.getView();
			String currentHeaderText;
			String currentFontName;
			Typeface currentFont;

			bodyText  = (TextView) view.findViewById(id.bodyTextView);
			
			switch (position) {
			case 0: {
				currentFontName = getFonts.getFontNameByID(position);
				currentFont = getFonts.getFontByID(position);
				break;
			}
			case 1: {
				currentFontName = getFonts.getFontNameByID(position);
				currentFont = getFonts.getFontByID(position);
				break;
			}
			case 2: {
				currentFontName = getFonts.getFontNameByID(position);
				currentFont = getFonts.getFontByID(position);
				break;
			}
			case 3: {
				currentFontName = getFonts.getFontNameByID(position);
				currentFont = getFonts.getFontByID(position);
				break;
			}
			default:;
				currentFontName = getFonts.getFontNameByID(0);
				currentFont = getFonts.getFontByID(0);
				break;
			}

			currentHeaderText = "Czcionka " + currentFontName + ", rozmiar " + textSize + ", "+ wordCount + " wyrazów.";		
			
			bodyText.setText(currentHeaderText+"\n"+textToView);
			bodyText.setTypeface(currentFont);
			bodyText.setTextColor(Color.WHITE);
			bodyText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
			
			return view;
		}
	}
}