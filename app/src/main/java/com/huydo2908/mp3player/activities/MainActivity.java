package com.huydo2908.mp3player.activities;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.huydo2908.basemodule.base.BaseActivity;
import com.huydo2908.basemodule.base.BaseFragment;
import com.huydo2908.mp3player.R;
import com.huydo2908.mp3player.databinding.ActivityMainBinding;
import com.huydo2908.mp3player.fragments.album.AlbumFragment;
import com.huydo2908.mp3player.fragments.artist.ArtistFragment;
import com.huydo2908.mp3player.fragments.song.SongFragment;
import com.huydo2908.mp3player.service.Mp3Service;

public class MainActivity extends BaseActivity<ActivityMainBinding> implements BottomNavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener, NavigationView.OnNavigationItemSelectedListener {

    private SongFragment fmSong = new SongFragment();
    private ArtistFragment fmArtist = new ArtistFragment();
    private AlbumFragment fmAlbum = new AlbumFragment();

    private SearchView searchView;
    private ActionBarDrawerToggle toggle;

    private Mp3Service service;

    @Override
    protected void init() {
        doRequestPermission(
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                new RequestPermissionCallBack() {
                    @Override
                    public void onGranted() {
                        Intent intent = new Intent(MainActivity.this, Mp3Service.class);
                        bindService(intent, connection, BIND_AUTO_CREATE);
                    }

                    @Override
                    public void onDenied() {
                        finish();
                    }
                });
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Mp3Service.Mp3Binder binder = (Mp3Service.Mp3Binder) service;
            MainActivity.this.service = binder.getService();

            initAct();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    public void initAct() {
        initFragment();
        binding.nav.setOnNavigationItemSelectedListener(MainActivity.this);

        toggle = new ActionBarDrawerToggle(
                MainActivity.this,
                binding.drawerLayout,
                R.string.drawer_open,
                R.string.drawer_close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        binding.drawerLayout.addDrawerListener(toggle);
        binding.drawer.setNavigationItemSelectedListener(MainActivity.this);
        binding.drawer.getMenu().getItem(0).setChecked(true);

        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void initFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.container, fmSong);
        transaction.add(R.id.container, fmAlbum);
        transaction.add(R.id.container, fmArtist);
        transaction.commit();
        showFragment(fmSong);
    }

    private void showFragment(BaseFragment fmShow) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(
                R.anim.fragment_enter,
                R.anim.fragment_exit);
        transaction.hide(fmSong);
        transaction.hide(fmAlbum);
        transaction.hide(fmArtist);
        transaction.show(fmShow);
        transaction.commit();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_song:
            case R.id.drawer_song:
                showFragment(fmSong);
                binding.nav.getMenu().getItem(0).setChecked(true);
                binding.drawer.getMenu().getItem(0).setChecked(true);
                break;
            case R.id.nav_album:
            case R.id.drawer_album:
                showFragment(fmAlbum);
                binding.nav.getMenu().getItem(1).setChecked(true);
                binding.drawer.getMenu().getItem(1).setChecked(true);
                break;
            case R.id.nav_artist:
            case R.id.drawer_artist:
                showFragment(fmArtist);
                binding.nav.getMenu().getItem(2).setChecked(true);
                binding.drawer.getMenu().getItem(2).setChecked(true);
                break;
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);

        searchView = (SearchView) menu.findItem(R.id.item_search).getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint(getString(R.string.search));

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        fmSong.executeSearch(s);
        fmAlbum.executeSearch(s);
        fmArtist.executeSearch(s);
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }

    public Mp3Service getService() {
        return service;
    }
}
