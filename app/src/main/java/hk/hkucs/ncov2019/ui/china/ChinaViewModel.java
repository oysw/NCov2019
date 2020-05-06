package hk.hkucs.ncov2019.ui.china;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONArray;

public class ChinaViewModel extends ViewModel {

    private MutableLiveData<JSONArray> mData;

    public ChinaViewModel() {
        mData = new MutableLiveData<JSONArray>();
    }

    public LiveData<JSONArray> getData() {
        return mData;
    }

    public void setData(JSONArray jsonArray){
        mData.postValue(jsonArray);
    }
}