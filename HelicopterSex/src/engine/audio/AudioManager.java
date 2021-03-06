package engine.audio;

import engine.Game;
import engine.GameTime;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;

public class AudioManager
{
	private static Map<String, Media> medias = new HashMap<String, Media>();
	private static Map<String, Double> mediaVolumes = new HashMap<String, Double>();
	private static Map<Long, MediaPlayer> mediaPlayers = new HashMap<Long, MediaPlayer>();
	private static Map<String, String> playedMedia = new HashMap<String, String>();
	
	private static long mediaPlayerIDGenerator = 0;

	private static double masterVolume = 0.0;
	public static double volumeStep = 0.05;
	public static double playbackSpeed = 1;
	
//	public static LinkedList<MediaPlayerContainer> playerContainers = new LinkedList<MediaPlayerContainer>();
//	public static Map<String, LinkedList<MediaPlayer>> usedMediaPlayers = new HashMap<String, LinkedList<MediaPlayer>>();
		

	public static void update(GameTime gameTime)
	{
//		ListIterator<MediaPlayerContainer> iterator = playerContainers.listIterator();
//		while(iterator.hasNext())
//		{
//			MediaPlayerContainer container = iterator.next();
//			container.update(gameTime);
//			if(container.isFinished)
//			{
//				iterator.remove();
//				LinkedList<MediaPlayer> list = usedMediaPlayers.get(container.mediaName);
//				list.addLast(container.player);
//			}
//		}
	}
	
	public static double getMasterVolume()
	{
		return masterVolume;
	}

	public static void setMasterVolume(double masterVolume)
	{
		if (masterVolume >= 0.0 && masterVolume <= 1.0)
		{
			AudioManager.masterVolume = masterVolume;
		}
	}

	public static void increaseMasterVolume()
	{
		masterVolume += volumeStep;
		if (masterVolume > 1.0)
		{
			masterVolume = 1.0;
		}
	}

	public static void decreaseMasterVolume()
	{
		masterVolume -= volumeStep;
		if (masterVolume < 0)
		{
			masterVolume = 0;
		}
	}

	public static long play(String mediaName, boolean loop)
	{
		Media media = medias.get(mediaName);
		MediaPlayer mediaPlayer = new MediaPlayer(media);
		mediaPlayer.setVolume(mediaVolumes.get(mediaName) * masterVolume);
		mediaPlayer.setRate(playbackSpeed);

		long mediaPlayerID = mediaPlayerIDGenerator++;
		mediaPlayers.put(mediaPlayerID, mediaPlayer);

		if (loop == true)
		{
			mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
		}
		mediaPlayer.play();

		return mediaPlayerID;
	}

	public static void play(long id)
	{
		MediaPlayer mediaPlayer = mediaPlayers.get(id);
		mediaPlayer.setRate(playbackSpeed);
		if (mediaPlayer != null)
		{
			mediaPlayer.play();
		}
	}

	public static void playOnceNoInterrupt(String mediaName)
	{
		String isPlaying = playedMedia.get(mediaName);
		if(isPlaying != null)
		{
			return;
		}
		
		playedMedia.put(mediaName, "playing");
		
		Media media = medias.get(mediaName);
		MediaPlayer player = new MediaPlayer(media);
		
		player.setVolume(mediaVolumes.get(mediaName) * masterVolume);
		player.setRate(playbackSpeed);
		// TODO: handle player, dispose?
//		player.setOnEndOfMedia(new OnEndOfMediaHandler(player));
		player.play();
		
//		LinkedList<MediaPlayer> players = usedMediaPlayers.get(mediaName);
//		Media media = medias.get(mediaName);
//		MediaPlayer player;
//		if(players == null)
//		{
//			players = new LinkedList<MediaPlayer>();
//			usedMediaPlayers.put(mediaName, players);
//			player = new MediaPlayer(media);
//		}
//		else
//		{
//			
//			if(players.size() == 0)
//			{
//				player = new MediaPlayer(media);
//			}
//			else
//			{
//				player = players.removeFirst();
//			}
//		}
//		
//		MediaPlayerContainer container = new MediaPlayerContainer(player, media, mediaName);
//		playerContainers.add(container);
//		
//		player.setVolume(mediaVolumes.get(mediaName) * masterVolume);
//		player.setRate(playbackSpeed);
//		player.play();
		
	}
	
	public static void playOnceWithoutTimeAlter(String mediaName)
	{
		
		Media media = medias.get(mediaName);
		MediaPlayer player = new MediaPlayer(media);
		player.setVolume(mediaVolumes.get(mediaName) * masterVolume);
		player.play();
		
	}

	public static void pause(long id)
	{
		MediaPlayer mediaPlayer = mediaPlayers.get(id);
		if (mediaPlayer != null)
		{
			mediaPlayer.pause();
		}
	}

	public static void stop(long id)
	{
		MediaPlayer mediaPlayer = mediaPlayers.get(id);
		if (mediaPlayer != null)
		{
			mediaPlayer.stop();
			mediaPlayers.remove(mediaPlayer);
		}
	}

	public static void loadMedia()
	{
		BufferedReader br;
		try
		{
			br = new BufferedReader(new FileReader("content/sounds/sounds.txt"));

			String line = br.readLine();

			while (line != null)
			{
				String[] tokens = line.split(" ");

				String filePath = "content/sounds/" + tokens[0] + ".mp3";
				String uri = Paths.get(filePath).toUri().toString();
				Media media = new Media(uri);

				medias.put(tokens[0], media);
				mediaVolumes.put(tokens[0], Double.parseDouble(tokens[1]));

				line = br.readLine();
			}

		} catch (Exception e)
		{
			System.err.println("Error reading file sounds.txt");
			Game.game.exitGame();
		}
	}

	public static void setPlaybackRate(long id, float timeScale)
	{
		MediaPlayer mediaPlayer = mediaPlayers.get(id);
		mediaPlayer.setRate(timeScale);
	}

	public static void filter()
	{
		playedMedia.clear();
	}
	
}
