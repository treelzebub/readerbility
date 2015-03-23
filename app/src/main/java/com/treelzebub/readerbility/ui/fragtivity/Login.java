package com.treelzebub.readerbility.ui.fragtivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.oauth.OAuthLoginActionBarActivity;
import com.treelzebub.readerbility.Constants;
import com.treelzebub.readerbility.R;
import com.treelzebub.readerbility.api.Readability;
import com.treelzebub.readerbility.api.ReadabilityApi;
import com.treelzebub.readerbility.auth.ReadabilityClient;
import com.treelzebub.readerbility.util.Settings;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Tre Murillo on 2/27/15
 * <p/>
 * An Activity which
 * 1) Redirects to Readability, where user logs in and
 * 2) retrieves an authorization code, so we can
 * 3) get the motherfuck on with our lives
 */
public class Login {

    public static class LoginActivity extends OAuthLoginActionBarActivity<ReadabilityClient> {
        public static final String TAG = "LoginActivity";

        @InjectView(R.id.auth_url_tv)
        TextView mAuthUrlTV;
        @InjectView(R.id.verifier_et)
        EditText mVerifierET;
        @InjectView(R.id.submit_button)
        Button mSubmitButton;
        @InjectView(R.id.login_progress)
        ProgressBar mProgressBar;

        private Token mRequestToken;
        private OAuthService sReadability;
        private String mAuthUrl;

        @Override
        protected void onStart() {
            super.onStart();
            new SetAuthUrl().execute();
        }

        @Override
        protected void onResume() {
            super.onResume();
            mVerifierET.requestFocus();
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);
            ButterKnife.inject(this);

            mProgressBar.setVisibility(View.GONE);
            mAuthUrlTV.setEllipsize(TextUtils.TruncateAt.END);
            mAuthUrlTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent webPageIntent = new Intent(Intent.ACTION_VIEW);
                    webPageIntent.setData(Uri.parse(mAuthUrl));
                    startActivity(webPageIntent);
                }
            });

            mSubmitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO
                    String codeFromUser = mVerifierET.getText().toString().trim();
                    Verifier verifier = new Verifier(codeFromUser);
                    new SetAccessToken().execute(verifier);
                }
            });
        }

        @Override
        public void onLoginSuccess() {
            startActivity(new Intent(this, Library.LibraryActivity.class));
        }

        @Override
        public void onLoginFailure(Exception e) {
            Toast.makeText(
                    getApplicationContext(),
                    "Authorization Failed. Please try again.",
                    Toast.LENGTH_LONG).show();
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_settings:
                    startActivity(new Intent(this, Settings.SettingsActivity.class));
                    return true;
                default:
                    break;
            }
            return false;
        }

        private class SetAuthUrl extends AsyncTask<Void, Void, String> {

            @Override
            protected String doInBackground(Void... params) {
                sReadability = new ServiceBuilder()
                        .provider(ReadabilityApi.class)
                        .apiKey(Constants.CONSUMER_KEY)
                        .apiSecret(Constants.CONSUMER_SECRET)
                        .build();
                mRequestToken = sReadability.getRequestToken();
                return sReadability.getAuthorizationUrl(mRequestToken);
            }

            @Override
            protected void onPostExecute(String authUrl) {
                super.onPostExecute(authUrl);
                mAuthUrl = authUrl;
                mAuthUrlTV.setText(authUrl);
            }

            @Override
            protected void onCancelled() {
                super.onCancelled();

            }
        }

        private class SetAccessToken extends AsyncTask<Verifier, Void, Token> {

            @Override
            protected Token doInBackground(Verifier... params) {
                return sReadability.getAccessToken(mRequestToken, params[0]);
            }

            @Override
            protected void onPostExecute(Token token) {
                super.onPostExecute(token);
                Readability.getInstance().setToken(token);
                startActivity(new Intent(getApplicationContext(), Library.LibraryActivity.class));
            }
        }
    }
}
