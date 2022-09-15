package josh.owen.dogbrowser.core.runners

import android.app.Application
import android.content.Context
import android.os.StrictMode
import androidx.test.runner.AndroidJUnitRunner
import josh.owen.dogbrowser.core.base.BaseApplicationTest_Application

class MockTestRunner : AndroidJUnitRunner() {

    override fun newApplication(cl: ClassLoader?, name: String?, context: Context?): Application {
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().permitAll().build())
        return super.newApplication(cl, BaseApplicationTest_Application::class.java.name, context)
    }
}