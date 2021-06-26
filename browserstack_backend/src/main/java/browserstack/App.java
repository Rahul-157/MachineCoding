package browserstack;

import java.util.*;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public final class App {
    
    public static void main(String[] args) {
        BrowserStack bs = new BrowserStack("Enter Your key here", "Enter your user name here");
        bs.getStatus();

        bs.getBrowsers();
        
        bs.getWorker();

        Map<String,String> mp = new HashMap<String,String>();
        mp.put("os", "OS X");
        mp.put("os_version","Catalina");
        mp.put("url", "https://github.com/browserstack/api");
        mp.put("browser", "firefox");
        mp.put("browser_version","75.0");
        String res = bs.createWorker(mp);

        HashMap<String,String> map = new Gson().fromJson(res, new TypeToken<HashMap<String, String>>(){}.getType());
        String worker_id = map.get("id");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        bs.getWorker(worker_id);
        
        bs.screenshot(worker_id, "png");
        
        bs.deleteWorker(worker_id);
    }
}
