package OOP2.Solution;

import OOP2.Provided.*;

import java.util.*;

public class FaceOOPImpl implements FaceOOP {

	private HashMap<Integer, Person> Graph;

	public class FeedIterator implements StatusIterator{
		Iterator<Status> current;
		Queue<Integer> friends;
		boolean RecentOrLikes; // if true then iterate byRecent otherwise by popularity

		public FeedIterator(Person p,boolean byRecent){
			friends = new LinkedList<Integer>();
			RecentOrLikes = byRecent;
			for(Person f : p.getFriends())
				friends.add(f.getId());
			current = byRecent ? p.getStatusesRecent().iterator() : p.getStatusesPopular().iterator();
		}

		@Override
		public boolean hasNext() {
			return current.hasNext()  || !friends.isEmpty();
		}

		@Override
		public Status next() {
			if(current.hasNext())
				return current.next();
			if(RecentOrLikes)
				current = Graph.get(friends.poll()).getStatusesRecent().iterator();
			else
				current = Graph.get(friends.poll()).getStatusesPopular().iterator();
			return current.next();
		}
	}

	/**
	 * Constructor - receives no parameters and initializes the system.
	 */
	public FaceOOPImpl() {
		this.Graph = new HashMap<Integer, Person>();
	}
	
	@Override
	public Person joinFaceOOP(Integer id, String name) throws PersonAlreadyInSystemException {
		if(Graph.containsKey(id))
			throw new PersonAlreadyInSystemException();
		Person p = new PersonImpl(id, name);
		Graph.put(id,p);
		return p;
	}

	@Override
	public int size() {
		return Graph.size();
	}

	@Override
	public Person getUser(Integer id) throws PersonNotInSystemException {
		if(!Graph.containsKey(id))
			throw new PersonNotInSystemException();
		return Graph.get(id);
	}

	@Override
	public void addFriendship(Person p1, Person p2) throws PersonNotInSystemException, SamePersonException, ConnectionAlreadyExistException {
		if(!Graph.containsKey(p1.getId()) || !Graph.containsKey(p2.getId()) )
			throw new PersonNotInSystemException();
		Graph.get(p1.getId()).addFriend(p2);
		Graph.get(p2.getId()).addFriend(p1);
	}

	@Override
	public StatusIterator getFeedByRecent(Person p) throws PersonNotInSystemException {
		if(!Graph.containsKey(p.getId()) )
			throw new PersonNotInSystemException();
		return new FeedIterator(p,true);
	}

	@Override
	public StatusIterator getFeedByPopular(Person p) throws PersonNotInSystemException {
		if(!Graph.containsKey(p.getId()) )
			throw new PersonNotInSystemException();
		return new FeedIterator(p,false);
	}

	@Override
	public Integer rank(Person source, Person target) throws PersonNotInSystemException, ConnectionDoesNotExistException {
		if(!Graph.containsKey(source.getId()) || !Graph.containsKey(target.getId()) )
			throw new PersonNotInSystemException();

		if(source.getId().equals(target.getId()))
			return 0;

		Queue<Integer> Q = new LinkedList<Integer>();
		HashSet<Integer> visited = new HashSet<Integer>();
		Q.add(source.getId());
		Integer distance=0;
		while(!Q.isEmpty()){
			int leveLen = Q.size(), current=0;
			for(int i=0 ; i<leveLen ; i++){
				current = Q.poll();
				visited.add(current);
				for(Person p : Graph.get(current).getFriends()){
					if(p.getId().equals(target.getId())) return distance+1;
					if(!visited.contains(p.getId())) Q.add(p.getId());
				}
			}
			distance++;
		}
		throw new ConnectionDoesNotExistException();
	}

	public class FaceIterator implements Iterator<Person>{

		Queue<Integer> sorted_ids;

		public FaceIterator(){
			sorted_ids = new LinkedList<Integer>();
			ArrayList<Integer> tmp = new ArrayList<Integer>(Graph.keySet());
			Collections.sort(tmp);
			for(Integer i : tmp) sorted_ids.add(i);
		}

		@Override
		public boolean hasNext() {
			return !sorted_ids.isEmpty();
		}

		@Override
		public Person next() {
			return Graph.get(sorted_ids.poll());
		}
	}

	public Iterator<Person> iterator(){
		return new FaceIterator();
	}
}
