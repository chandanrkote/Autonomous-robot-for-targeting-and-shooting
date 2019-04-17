package at.abraxas.amarino;

import android.content.Context;
import android.content.SharedPreferences;

import com.parse.Parse;
import com.parse.ParseObject;

public class Application extends android.app.Application {
  // Debugging switch
  public static final boolean APPDEBUG = false;
  
  // Debugging tag for the application
  public static final String APPTAG = "AnyWall";

  // Key for saving the search distance preference
  private static final String KEY_SEARCH_DISTANCE = "searchDistance";

  private static SharedPreferences preferences;

  public Application() {
	    
  }
   
  @Override  
  public void onCreate() {
    super.onCreate();  
    
    Parse.initialize(this, "5TbY4yXuNz0TJiv0T8F7SHqnrn3gWRJzUh0mZKzk",
        "JdxAA05G58az0PVJxhlElesIq3QcLeq6qSKkBez7");
    preferences = getSharedPreferences("com.parse.anywall", Context.MODE_PRIVATE);
      
  }
  
  public static float getSearchDistance() {
    return preferences.getFloat(KEY_SEARCH_DISTANCE, 250);
  }

  public static void setSearchDistance(float value) {
    preferences.edit().putFloat(KEY_SEARCH_DISTANCE, value).commit();
  }

}
