package com.google;

import java.util.ArrayList;

/** A class used to represent a Playlist */
class VideoPlaylist {
	private String name;
	private ArrayList<String> videoIds;

	VideoPlaylist(String name) {
		this.name = name;
		this.videoIds = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	public ArrayList<String> getVideoIds() {
		return this.videoIds;
	}

	public Boolean containsVideo(String videoId) {
		for (var Vid: getVideoIds()) {
			if (Vid.equals(videoId)) {
				return true;
			}
		}
		return false;
	}

	public void addVideo(String videoId) {
		videoIds.add(videoId);
	}

	public void removeVideo(String videoId) {
		videoIds.remove(videoId);
	}

	public Boolean isEmpty() {
		return videoIds.isEmpty();
	}

	public void clear() {
		videoIds.clear();
	}

}
