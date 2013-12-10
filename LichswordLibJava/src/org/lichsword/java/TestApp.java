package org.lichsword.java;

import java.io.File;

import org.lichsword.java.os.AsyncTask;
import org.lichsword.java.xml.dom.DOMParseManager;

public class TestApp {

    /**
     * @param args
     */
    public static void main(String[] args) {
        DOMParseManager.getInstance(new File("prefs.xml")).putInt("key1", 10086);
        MyTask task = new MyTask();
        task.execute("hello", "world", "China");

    }

    static class MyTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub

        }

        @Override
        protected Integer doInBackground(String... params) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        protected void onPostExecute() {
            // TODO Auto-generated method stub

        }

    }
}
