package com.example.arcgis_map;

import static android.content.ContentValues.TAG;
import static android.view.View.VISIBLE;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.PointCollection;
import com.esri.arcgisruntime.geometry.Polygon;
import com.esri.arcgisruntime.geometry.Polyline;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.BasemapStyle;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.SimpleFillSymbol;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.tasks.geocode.GeocodeResult;
import com.esri.arcgisruntime.tasks.geocode.LocatorTask;
import com.esri.arcgisruntime.tasks.geocode.SuggestParameters;
import com.esri.arcgisruntime.tasks.geocode.SuggestResult;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private androidx.appcompat.widget.SearchView SearchView;
    private ListView listview;
    String namelist[]={"Venkat","Tamil","Sumo","Nadi","Ammu","Virat","Dhoni","Gambhir"};
    ArrayList<String> ListSuggestion = new ArrayList<String>();
    ArrayAdapter<String> arrayAdapter;

    @RequiresApi(api = Build.VERSION_CODES.N)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MapView mapview = findViewById(R.id.MapView);
        SearchView=findViewById(R.id.Searchtext);
        listview=findViewById(R.id.Listview);
        listview.setVisibility(View.INVISIBLE);

        //ArcGisMap integrate into out map view
//        ArcGISMap arcGisMap = new ArcGISMap(Basemap.Type.OPEN_STREET_MAP, 13.0827, 80.2707, 13);
//        mapview.setMap(arcGisMap);

        //Without any viewpoint(lat and long)
//        ArcGISMap map=new ArcGISMap(Basemap.createOpenStreetMap());
//        mapview.setMap(map);

        //with viewpoint
//        ArcGISMap map=new ArcGISMap(Basemap.createOpenStreetMap());
//        mapview.setMap(map);
//        Point point=new Point(80.2707,13.0827, SpatialReferences.getWgs84());
//        Viewpoint viewpoint=new Viewpoint(point,2000);
//        mapview.setViewpointAsync(viewpoint);

        //with viewpoint and duration

        //ADD A BASE MAP TO APP:
        ArcGISRuntimeEnvironment.setApiKey("AAPK026861e3641744818c90e0ae6c270d12Dg_pj59bbpWVU2LC2BztipOpD4DAmGBtw-PbeQhCQGeiyl5xeW2izNTyHmGr-ND-");
        ArcGISMap map=new ArcGISMap(BasemapStyle.ARCGIS_STREETS);
        mapview.setMap(map);
        Point point=new Point(80.2707,13.0827, SpatialReferences.getWgs84());
        Viewpoint viewpoint=new Viewpoint(point,2000);
        mapview.setViewpointAsync(viewpoint,3);
        GraphicsOverlay graphicsOverlay = new GraphicsOverlay();
        mapview.getGraphicsOverlays().add(graphicsOverlay);
//--------------------------------------------------------------Basic setup code----------------------------------------------------------------
/*
        //A graphics overlay is a container for graphics. It is used with a map view to display graphics on a map.
        // You can add more than one graphics overlay to a map view. Graphics overlays are displayed on top of all the other layers.
        GraphicsOverlay graphicsOverlay = new GraphicsOverlay();
        mapview.getGraphicsOverlays().add(graphicsOverlay);

        //ADD A POINT TO THE BASEMAP:

        // create a point geometry with a location and spatial reference
        Point point1 = new Point(80.2707,13.0827, SpatialReferences.getWgs84());
        // create an opaque orange (0xFFFF5733) point symbol with a blue (0xFF0063FF) outline symbol
        SimpleMarkerSymbol simpleMarkerSymbol =
                new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, 0xFFFF5733, 10);
        SimpleLineSymbol blueOutlineSymbol =
                new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, 0xFF0063FF, 2);

        simpleMarkerSymbol.setOutline(blueOutlineSymbol);

        // create a graphic with the point geometry and symbol
        Graphic pointGraphic = new Graphic(point1, simpleMarkerSymbol);

        // add the point graphic to the graphics overlay
        graphicsOverlay.getGraphics().add(pointGraphic);


        //ADD A LINE TO BASEMAP:

        // create a point collection with a spatial reference, and add three points to it
        PointCollection polylinePoints = new PointCollection(SpatialReferences.getWgs84());
        polylinePoints.add(new Point(80.2707, 13.0827));
        polylinePoints.add(new Point(78.1198, 9.9252));
        polylinePoints.add(new Point(77.5385, 8.0883));

        // create a polyline geometry from the point collection
        Polyline polyline = new Polyline(polylinePoints);

        // create a blue line symbol for the polyline
        SimpleLineSymbol polylineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, 0xFF0063FF, 3);

        // create a polyline graphic with the polyline geometry and symbol
        Graphic polylineGraphic = new Graphic(polyline, polylineSymbol);

        // add the polyline graphic to the graphics overlay
        graphicsOverlay.getGraphics().add(polylineGraphic);

        //ADD A POLYGON TO OUT BASEMAP:

        // create a point collection with a spatial reference, and add five points to it
        PointCollection polygonPoints = new PointCollection(SpatialReferences.getWgs84());
        polygonPoints.add(new Point(80.2707, 13.0827));
        polygonPoints.add(new Point(78.1198, 9.9252));
        polygonPoints.add(new Point(77.5385, 8.0883));
        // create a polygon geometry from the point collection
        Polygon polygon = new Polygon(polygonPoints);

        // create an orange fill symbol with 20% transparency and the blue simple line symbol
        SimpleFillSymbol polygonFillSymbol = new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, 0x80FF5733, blueOutlineSymbol);

        // create a polygon graphic from the polygon geometry and symbol
        Graphic polygonGraphic = new Graphic(polygon, polygonFillSymbol);
        // add the polygon graphic to the graphics overlay
        graphicsOverlay.getGraphics().add(polygonGraphic);
*/

//---------------------------------------------------------------Auto Suggest Code--------------------------------------------------------------
        LocatorTask locatorTask = new LocatorTask("https://geocode-api.arcgis.com/arcgis/rest/services/World/GeocodeServer");


//----------------------------------------------------------------Searchview code---------------------------------------------------------------


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listview.setVisibility(View.INVISIBLE);
                Toast.makeText(MainActivity.this, "You clicked "+ parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
                ListenableFuture<List<GeocodeResult>> geocodeResultsFuture = locatorTask.geocodeAsync(parent.getItemAtPosition(position).toString());
                geocodeResultsFuture.addDoneListener(() -> {
                    try {
                        List<GeocodeResult> geocodeResults = geocodeResultsFuture.get();
                        if(geocodeResults.size() > 0) {
                            GeocodeResult geocodeResult = geocodeResults.get(0);
                            Point clickpoint=new Point(geocodeResult.getDisplayLocation().getX(),geocodeResult.getDisplayLocation().getY(), SpatialReferences.getWgs84());
                            SimpleMarkerSymbol simpleMarkerSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, 0xFFFF5733, 10);
                            Graphic pointGraphic = new Graphic(clickpoint, simpleMarkerSymbol);
                            graphicsOverlay.getGraphics().add(pointGraphic);
                            Viewpoint clickviewpoint=new Viewpoint(clickpoint,2000);
                            mapview.setViewpointAsync(clickviewpoint,3);
                        }
                    } catch (Exception e) { e.printStackTrace(); }
                });
            }
        });

        //Filter from listview:

        SearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                listview.setVisibility(View.VISIBLE);
                ListenableFuture<List<SuggestResult>> SuggestResultFuture=locatorTask.suggestAsync(query);
                SuggestResultFuture.addDoneListener(()->{
                    try {
                        List<SuggestResult> Suggestions=SuggestResultFuture.get();

                        Suggestions.forEach(i -> ListSuggestion.add(i.getLabel()));
                        Log.d(TAG, "suggestion size: "+Suggestions.size());
                        Log.d(TAG, "list suggestion size: "+ ListSuggestion.size());
                        ListSuggestion.forEach(i -> System.out.println(i));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                arrayAdapter=new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,ListSuggestion);
                listview.setAdapter(arrayAdapter);

                MainActivity.this.arrayAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ListenableFuture<List<SuggestResult>> SuggestResultFuture=locatorTask.suggestAsync(newText);
                SuggestResultFuture.addDoneListener(()->{
                    try {
                        List<SuggestResult> Suggestions=SuggestResultFuture.get();

                        Suggestions.forEach(i -> ListSuggestion.add(i.getLabel()));
                        Log.d(TAG, "suggestion size: "+Suggestions.size());
                        Log.d(TAG, "list suggestion size: "+ ListSuggestion.size());
                        ListSuggestion.forEach(i -> System.out.println(i));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                arrayAdapter=new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,ListSuggestion);
                listview.setAdapter(arrayAdapter);
                listview.setVisibility(View.VISIBLE);
                MainActivity.this.arrayAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

}