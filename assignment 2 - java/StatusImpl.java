package OOP2.Solution;
import OOP2.Provided.Status;
import OOP2.Provided.Person;
import java.util.*;

public class StatusImpl implements Status, Comparator<Status> {
	private final Person publisher;
	private final String content;
	private final Integer id;
	private final Collection<Person> likes;
	/*
	 * A constructor that receives the status publisher, the text of the status
	 *  and the id of the status.
	 */
	public StatusImpl(Person publisher, String content, Integer id)
	{
		this.publisher = publisher;
		this.content = content;
		this.id = id;
		likes = new TreeSet<>();
	}

	public Integer getId()
	{
		return id;
	}
	public Person getPublisher()
	{
		return publisher;
	}
	public String getContent()
	{
		return content;
	}

	public void like(Person p)
	{
		likes.add(p);
	}

	public void unlike(Person p)
	{
		likes.remove(p);
	}

	public Integer getLikesCount()
	{
		return likes.size();
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Status))
			return false;
		return ((Status)o).getId().equals(id) && ((Status)o).getPublisher().equals(publisher);
	}

	public int compare(Status s1, Status s2) {
		return s1.getLikesCount().compareTo(s2.getLikesCount());
	}
}
