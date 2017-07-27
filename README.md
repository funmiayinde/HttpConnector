HttpConnector is a minimalistic HTTP client for android capable of making post and get requests. I wanted a simple http client for android and tried ION it kept failing on android 4.* < and some samsung devices so I thought to create a simple HttpUrlConnection wrapper. I hope you find it useful

Version

0.0.1

Tech

HttpConnector has only one dependency GSON v 2.6

Installation

# Usage

public void makeRequest(final Object object) { progressDialog.setIndeterminate(true); progressDialog.setMessage("please wait..."); progressDialog.show(); final MakeRequest request = HttpBuilder.create(this); request.post(Constant.SIGN_UP_URL) .params(object) .contentType("application/json; charset=utf-8") .handler(new ResponseHandler() {

                @Override
                public void onSuccess(String responseData, Response response) throws JSONException {
                    progressDialog.dismiss();
                    JSONObject jsonResponse = new JSONObject(responseData);

                    if (jsonResponse.optBoolean("success")) {

                        JSONObject jsonData = jsonResponse.getJSONObject("data");
                        String name = jsonData.getString("name");
                        String phone = jsonData.getString("phone");

                        Intent intent = new Intent(SignUpActivity.this, OtpActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                        finishAffinity();
                    }

                }

                @Override
                public void onError(String message, Response response) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(NetworkError networkError) {
                    super.onFailure(networkError);
                    progressDialog.dismiss();
                    if (networkError.equals(NetworkError.OFFLINE)) {
                        Toast.makeText(getApplicationContext(), "Check your internet connection", Toast.LENGTH_LONG).show();
                    }

                    if (networkError.equals(NetworkError.TIMEOUT)){
                        Toast.makeText(getApplicationContext(), "server does not response, please try again", Toast.LENGTH_LONG).show();
                    }

                    if (networkError.equals(NetworkError.UNKNOWN)){
                        Toast.makeText(getApplicationContext(), "an error occur while processing your data,please try again", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onComplete() {
                    super.onComplete();
                    progressDialog.dismiss();
                }
            }).execute();
}
License

Apache 2.0

Free Software!
