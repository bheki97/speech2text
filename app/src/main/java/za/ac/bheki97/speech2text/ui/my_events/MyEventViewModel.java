package za.ac.bheki97.speech2text.ui.my_events;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyEventViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public MyEventViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}