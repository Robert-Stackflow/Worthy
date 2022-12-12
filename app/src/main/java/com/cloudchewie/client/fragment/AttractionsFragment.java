package com.cloudchewie.client.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cloudchewie.client.R;
import com.cloudchewie.client.adapter.AttractionsAdapter;
import com.cloudchewie.client.domin.Attraction;

import java.util.ArrayList;
import java.util.List;

public class AttractionsFragment extends Fragment implements View.OnClickListener {
    View mainView;
    List<Attraction> attractions;
    AttractionsAdapter attractionsAdapter;
    RecyclerView attractionsRecyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = View.inflate(getContext(), R.layout.fragment_attractions, null);
        attractionsRecyclerView = mainView.findViewById(R.id.attractions_recyclerview);
        attractions = new ArrayList<>();
        attractions.add(new Attraction("九寨沟", "四川省九寨沟市黄果山瀑布", "风景宜人的千古名胜", 1, 23.3, 34.6, 100, 200, 300));
        attractions.add(new Attraction("东湖", "湖北省武汉市洪山区", "凌波门畔，赏日出绝景", 1, 23.3, 34.6, 123, 3, 22));
        attractions.add(new Attraction("珞珈山", "湖北省武汉市武汉大学内", "赏樱之人络绎不绝，且看珞珈山下热舞", 1, 23.3, 34.6, 133, 24, 45));
        attractions.add(new Attraction("少林寺", "河南省洛阳市东山县少林寺", "这里不仅是少林寺，更是佛教圣地", 1, 23.3, 34.6, 22, 54, 12));
        attractions.add(new Attraction("故宫", "北京市中心故宫园区", "千朝古都，泱泱大国之风范", 1, 23.3, 34.6, 234, 65, 14));
        attractions.add(new Attraction("九寨沟", "四川省九寨沟市黄果山瀑布", "风景宜人的千古名胜", 1, 23.3, 34.6, 100, 200, 300));
        attractions.add(new Attraction("东湖", "湖北省武汉市洪山区", "凌波门畔，赏日出绝景", 1, 23.3, 34.6, 123, 3, 22));
        attractions.add(new Attraction("珞珈山", "湖北省武汉市武汉大学内", "赏樱之人络绎不绝，且看珞珈山下热舞", 1, 23.3, 34.6, 133, 24, 45));
        attractions.add(new Attraction("少林寺", "河南省洛阳市东山县少林寺", "这里不仅是少林寺，更是佛教圣地", 1, 23.3, 34.6, 22, 54, 12));
        attractions.add(new Attraction("故宫", "北京市中心故宫园区", "千朝古都，泱泱大国之风范", 1, 23.3, 34.6, 234, 65, 14));
        attractionsAdapter = new AttractionsAdapter(getActivity(), attractions);
        attractionsRecyclerView.setAdapter(attractionsAdapter);
        attractionsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return mainView;
    }

    @Override
    public void onClick(View view) {
    }
}
