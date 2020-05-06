package hk.hkucs.ncov2019.ui.news;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONArray;

public class NewsViewModel extends ViewModel {

    private MutableLiveData<JSONArray> mData;

    public NewsViewModel() {
        mData = new MutableLiveData<>();
    }

    public LiveData<JSONArray> getData() {
        return mData;
    }

    public void setData(JSONArray jsonArray){
        mData.postValue(jsonArray);
    }
}