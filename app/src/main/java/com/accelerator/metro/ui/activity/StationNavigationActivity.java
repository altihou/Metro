package com.accelerator.metro.ui.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.accelerator.metro.R;
import com.accelerator.metro.bean.MapData;
import com.onlylemi.mapview.library.MapView;
import com.onlylemi.mapview.library.MapViewListener;
import com.onlylemi.mapview.library.layer.MarkLayer;
import com.onlylemi.mapview.library.layer.RouteLayer;
import com.onlylemi.mapview.library.utils.MapUtils;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class StationNavigationActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.mapview)
    MapView mapView;

    private MarkLayer markLayer;
    private RouteLayer routeLayer;

    private List<PointF> nodes;
    private List<PointF> nodesContract;
    private List<PointF> marks;
    private List<String> marksName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_navigation);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(view -> finish());

        nodes = MapData.getNodesList();
        nodesContract = MapData.getNodesContactList();
        marks = MapData.getMarks();
        marksName = MapData.getMarksName();
        MapUtils.init(nodes.size(), nodesContract.size());

        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getAssets().open("map.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mapView.loadMap(bitmap);
        mapView.setMapViewListener(new MapViewListener() {
            @Override
            public void onMapLoadSuccess() {
                routeLayer = new RouteLayer(mapView);
                mapView.addLayer(routeLayer);

                markLayer = new MarkLayer(mapView, marks, marksName);
                mapView.addLayer(markLayer);
                markLayer.setMarkIsClickListener(num -> {
                    PointF target = new PointF(marks.get(num).x, marks.get(num).y);
                    List<Integer> routeList = MapUtils.getShortestDistanceBetweenTwoPoints
                            (marks.get(39), target, nodes, nodesContract);
                    routeLayer.setNodeList(nodes);
                    routeLayer.setRouteList(routeList);
                    mapView.refresh();
                });
                mapView.refresh();
            }

            @Override
            public void onMapLoadFail() {
            }

        });

    }

}
