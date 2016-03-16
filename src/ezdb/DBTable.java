package ezdb;

public interface DBTable {
	
	public void append(Object obj);
	
	public void update(Object obj);
	
	@SuppressWarnings("rawtypes")
	public Iterable selectCond(String cond);
	@SuppressWarnings("rawtypes")
	public Iterable select(Object obj);
	@SuppressWarnings("rawtypes")
	public Iterable selectNULL(Object obj);
	@SuppressWarnings("rawtypes")
	public Iterable selectE(Object... obj);
	
	public void delete(Object obj);
	public void deleteCond(String cond);
	public void deleteAll();
	
	public int count();
	public int countCond(String cond);
	
	public void commit();
	public void revoke();
	
	public void setAutoCommit(boolean val);
	public boolean getAutoCommit();
}
