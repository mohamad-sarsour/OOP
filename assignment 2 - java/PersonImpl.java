package OOP2.Solution;

import OOP2.Provided.ConnectionAlreadyExistException;
import OOP2.Provided.Person;
import OOP2.Provided.SamePersonException;
import OOP2.Provided.Status;

import java.util.*;

/*public class byLikesComp implements Comparator<Status>{
	public int compare(Status s1, Status s2) {
		int tmp = s2.getLikesCount().compareTo(s1.getLikesCount());
		if(tmp != 0) return tmp;
		else return s2.getId().compareTo(s1.getId());
	}
}*/
public class PersonImpl implements Person {

	private final Integer id;
	private final String name;
	private final Collection<Status> recentStatues;
	private final Collection<Person> friends;

	/**
	 * Constructor receiving person's id and name.
	 */
	public PersonImpl(Integer id, String name) {
		this.id = id;
		this.name = name;
		Comparator<Status> byIdComp = (s1,s2) -> s2.getId().compareTo(s1.getId());
		this.recentStatues = new TreeSet<>(byIdComp);
		this.friends = new TreeSet<>();
	}

	/**
	 * @return the person's id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @return the person's name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Adds a new status to the person's collection of statuses.
	 *
	 * @param content - the status content
	 * @return the new status
	 */
	public Status postStatus(String content) {
		Status s = new StatusImpl(this, content, recentStatues.size());
		recentStatues.add(s);
		return s;
	}

	/**
	 * Add a connection between this Person and another.
	 *
	 * @param p the Person to be added as a friend
	 * @throws SamePersonException             if p is the same person as the current instance.
	 * @throws ConnectionAlreadyExistException if p is already a friend of this person
	 */
	public void addFriend(Person p)
			throws SamePersonException, ConnectionAlreadyExistException
	{
		if(p.getId().equals(id))
		{
			throw new SamePersonException();
		}
		if(friends.contains(p))
		{
			throw new ConnectionAlreadyExistException();
		}
		friends.add(p);
	}

	/**
	 * @return collection of this person's friends
	 */
	public Collection<Person> getFriends()
	{
		return friends;
	}

	/**
	 * @return an iterable collection of all the person's statuses.
	 * 		The statuses are sorted by chronological descending order
	 * 		(LIFO order - last posted are first returned by iterator).
	 */
	public Iterable<Status> getStatusesRecent()
	{
		return recentStatues;
	}

	/**
	 * @return an iterable collection of all the person's statuses.
	 * 		The statuses are sorted by descending order of number of likes.
	 */
	public Iterable<Status> getStatusesPopular()
	{
		//Comparator<Status> byLikesComp = (s1,s2) -> s2.getLikesCount().compareTo(s1.getLikesCount());
		Comparator<Status> byLikesComp = (s1,s2) -> {
			int tmp = s2.getLikesCount().compareTo(s1.getLikesCount());
			if(tmp != 0) return tmp;
			return s2.getId().compareTo(s1.getId());
		};
		Collection<Status> popularStatues = new TreeSet<>(byLikesComp);
		popularStatues.addAll(recentStatues);
		return popularStatues;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Person))
			return false;
		return ((Person)o).getId().equals(id);
	}

	public int compareTo(Person p) {
		return this.id.compareTo(p.getId());
	}


}
