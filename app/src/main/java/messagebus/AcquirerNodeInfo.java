package messagebus;

/**
 *
 * @author abhishekmukhopadhyay
 */
public final class AcquirerNodeInfo
{

    private String acq_node_name;
    private String pname;
    private int timeout;
    private String listenip;
    private int listenport;
    private int backlog;
    private String acq_keyname;

    public AcquirerNodeInfo()
    {
    }

    public AcquirerNodeInfo(String acq_node_name, String pname, int timeout, String listenip, int listenport, int backlog, String acq_keyname)
    {
        this.acq_node_name = acq_node_name;
        this.pname = pname;
        this.timeout = timeout;
        this.listenip = listenip;
        this.listenport = listenport;
        this.backlog = backlog;
        this.acq_keyname = acq_keyname;
    }

    public String getAcq_node_name()
    {
        return acq_node_name;
    }

    public void setAcq_node_name(String acq_node_name)
    {
        this.acq_node_name = acq_node_name;
    }

    public String getPname()
    {
        return pname;
    }

    public void setPname(String pname)
    {
        this.pname = pname;
    }

    public int getTimeout()
    {
        return timeout;
    }

    public void setTimeout(int timeout)
    {
        this.timeout = timeout;
    }

    public String getListenip()
    {
        return listenip;
    }

    public void setListenip(String listenip)
    {
        this.listenip = listenip;
    }

    public int getListenport()
    {
        return listenport;
    }

    public void setListenport(int listenport)
    {
        this.listenport = listenport;
    }

    public int getBacklog()
    {
        return backlog;
    }

    public void setBacklog(int backlog)
    {
        this.backlog = backlog;
    }

    public String getAcq_keyname()
    {
        return acq_keyname;
    }

    public void setAcq_keyname(String acq_keyname)
    {
        this.acq_keyname = acq_keyname;
    }
}
