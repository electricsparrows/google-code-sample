package com.google;

import java.util.*;

public class VideoPlayer {

  private final VideoLibrary videoLibrary;
  private Map<String, VideoPlaylist> playlists;   //<name, Video>
  private Video currentPlaying = null;
  private Map<String, Video> paused;

  public VideoPlayer() {
    this.videoLibrary = new VideoLibrary();
    this.playlists = new HashMap<>();
    //TODO fix the pause video function
    this.paused = new HashMap<>();
  }

  public void numberOfVideos() {
    System.out.printf("%s videos in the library%n", videoLibrary.getVideos().size());
  }

  public void showAllVideos() {
    System.out.println("Here's a list of all available videos:");
    for (var vid : videoLibrary.getVideos()) {
      System.out.println(vid);
    }
  }

  /*
    Play the specified video. If a video is currently playing, display a note that this video will be
    stopped, even if the same video is already playing. If the video doesn’t exist, display a warning
    message (and don’t stop the currently playing video)
   */
  public void playVideo(String videoId) {
    //check if videoId is in videoLibrary first
    if (!videoLibrary.containsVideo(videoId) ) {
      System.out.println("Cannot play video: Video does not exist");
    } else {

      var video = videoLibrary.getVideo(videoId);

      if (currentPlaying != null) {
        stopVideo();
      }
      currentPlaying = videoLibrary.getVideo(videoId);
      System.out.println("Playing video: " + video.getTitle());
    }
  }

  /*
    Stop the current playing video. If no video is currently playing, display a warning message
    “Cannot stop video: No video is currently playing” and do nothing.
   */
  public void stopVideo() {
    if (currentPlaying == null) {
      System.out.println("Cannot stop video: No video is currently playing");
    } else {
      System.out.println("Stopping video: " + currentPlaying.getTitle());
      currentPlaying = null;
    }

  }

  public void playRandomVideo() {
    playVideo(videoLibrary.getRandomVideoId());
  }

  /**
    Pause the current playing video.
   If a video is already paused, display a warning message and do nothing.
   Equally, If no video is currently playing, display a warning message and do nothing
   **/
  public void pauseVideo() {
    if (currentPlaying == null) {
      System.out.println("Cannot pause video: No video is currently playing");
    } else if (paused.containsKey(currentPlaying.getVideoId())) {
      System.out.println("Video already paused: " + currentPlaying.getTitle());
    } else {
      System.out.println("Pausing video: " + currentPlaying.getTitle());
      paused.put(currentPlaying.getVideoId(), currentPlaying);
    }
  }

  public void continueVideo() {
    if (currentPlaying == null) {
      System.out.println("Cannot continue video: No video is currently playing");
    } else {
      if (paused.containsKey(currentPlaying.getVideoId())) {
        System.out.println("Continuing video: " + currentPlaying.getTitle());
        paused.remove(currentPlaying.getVideoId());
      } else {
        System.out.println("Cannot continue video: Video is not paused");
      }
    }
  }

  public void showPlaying() {
    if (currentPlaying == null) {
      System.out.println("No video is currently playing");
    } else {
      System.out.println("Currently playing: " + currentPlaying);
    }
  }

  /**
   * Create a new (empty) playlist with a unique name. If a playlist with the same name already exists,
   * display a warning to the user and do nothing.
   * @param playlistName - The playlist name should be a string with no whitespace.
   */
  public void createPlaylist(String playlistName) {
    if (playlistExists(playlistName)) {
      System.out.println("Cannot create playlist: A playlist with the same name already exists");
    } else {
      playlists.put(playlistName.toUpperCase(Locale.ROOT), new VideoPlaylist(playlistName));
      System.out.println("Successfully created new playlist: " + playlistName);
    }
  }

  /**
   * Adds the specified video to a playlist.
   * If either the video or the playlist don’t exist, show a warning
   * No duplicate videos; displays a warning message if user attempts to add a duplicate
   *
   * @param playlistName - name of playlist to add given video to
   * @param videoId - reference to video which user wants to add to the playlist
   */
  public void addVideoToPlaylist(String playlistName, String videoId) {
    if (playlistExists(playlistName) && videoLibrary.containsVideo(videoId)) {
      var vp = playlists.get(playlistName.toUpperCase(Locale.ROOT));

      if (vp.containsVideo(videoId)) {
        System.out.println(String.format("Cannot add video to %s: Video already added", playlistName));
      } else {
        vp.addVideo(videoId);
        System.out.println(String.format("Added video to %s: %s", playlistName, videoLibrary.getVideo(videoId).getTitle()));
      }

    } else if (playlistExists(playlistName)) {
      System.out.println(String.format("Cannot add video to %s: Video does not exist", playlistName));
    } else {
      System.out.println(String.format("Cannot add video to %s: Playlist does not exist", playlistName));
    }

  }

  /**
   * Show all the available playlists (name only).
   * The playlists are shown in lexicographical order by playlist name, case-insensitive.
   * playlist names are displayed in the same case that they were
   * originally created in the CREATE_PLAYLIST command.
   */
  public void showAllPlaylists() {
    if (playlists.isEmpty()) {
      System.out.println("No playlists exist yet");
    } else {
      List<String> allPlaylistNames = new ArrayList<>(playlists.keySet());
      Collections.sort(allPlaylistNames);

      System.out.println("Showing all playlists: ");
      for (String name: allPlaylistNames) {
        System.out.println(playlists.get(name).getName());
      }
    }
  }

  /**
   * Show all the videos in the specified playlist in the following format: “title (video_id) [tags]”.
   * The videos are listed in the same order they were added.
   * @param playlistName
   */
  public void showPlaylist(String playlistName) {
    if (playlistExists(playlistName)) {
      var pl = playlists.get(playlistName.toUpperCase(Locale.ROOT));
      System.out.println(String.format("Showing playlist: %s", playlistName));

      if (pl.isEmpty()) {
        System.out.println("No videos here yet");
      } else {
        for (String vid: pl.getVideoIds()) {
          System.out.println(videoLibrary.getVideo(vid));
        }
      }

    } else {
      System.out.println(String.format("Cannot show playlist %s: Playlist does not exist", playlistName));
    }

  }


  /**
   * Remove the specified video from the specified playlist.
   * If either does not exist, display a relevant warning message -- playlist existence is checked first
   * If video is not in the playlist, warning is displayed and nothing is done.
   *
   * @param playlistName
   * @param videoId
   */
  public void removeFromPlaylist(String playlistName, String videoId) {
    if (playlistExists(playlistName) && videoLibrary.containsVideo(videoId)) {

      var pl = playlists.get(playlistName.toUpperCase(Locale.ROOT));
      if (pl.containsVideo(videoId)) {
        pl.removeVideo(videoId);
        System.out.println(String.format("Removed video from %s: %s", playlistName, videoLibrary.getVideo(videoId).getTitle()));
      } else {
        System.out.println(String.format("Cannot remove video from %s: Video is not in playlist", playlistName));
      }

    } else if ( playlistExists(playlistName) ) {
      System.out.println(String.format("Cannot remove video from %s: Video does not exist", playlistName));
    } else {
      System.out.println(String.format("Cannot remove video from %s: Playlist does not exist", playlistName));
    }
  }


  /**
   * Removes all the videos from the playlist, but doesn’t delete the playlist itself.
   * If playlist doesn't exist, display warning.
   * @param playlistName
   */
  public void clearPlaylist(String playlistName) {
    if (playlistExists(playlistName)) {
      var vp = playlists.get(playlistName.toUpperCase(Locale.ROOT));
      vp.clear();
      System.out.println(String.format("Successfully removed all videos from %s", playlistName));

    } else {
      System.out.println(String.format("Cannot clear playlist %s: Playlist does not exist", playlistName));
    }
  }

  /**
   * Delete the specified playlist. Display a warning if the playlist doesn’t exist.
   * @param playlistName
   */
  public void deletePlaylist(String playlistName) {
    if (playlistExists(playlistName)) {
      playlists.remove(playlistName.toUpperCase(Locale.ROOT));
      System.out.println(String.format("Deleted playlist: %s", playlistName));
    } else {
      System.out.println(String.format("Cannot delete playlist %s: Playlist does not exist", playlistName));
    }
  }

  /** Checks if playlist is present in list of playlists
   *
   * @param playlistName - name of target playlist
   * @return Boolean - indicates existence of playlist with given playlist name
   */
  private boolean playlistExists(String playlistName) {
    return playlists.containsKey(playlistName.toUpperCase(Locale.ROOT));
  }

  public void searchVideos(String searchTerm) {
    System.out.println("searchVideos needs implementation");
  }

  public void searchVideosWithTag(String videoTag) {
    System.out.println("searchVideosWithTag needs implementation");
  }

  public void flagVideo(String videoId) {
    System.out.println("flagVideo needs implementation");
  }

  public void flagVideo(String videoId, String reason) {
    System.out.println("flagVideo needs implementation");
  }

  public void allowVideo(String videoId) {
    System.out.println("allowVideo needs implementation");
  }
}