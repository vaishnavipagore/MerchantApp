package utils;

import java.util.ArrayList;

/**
 *
 * @author Abhy
 */
public final class Action
{

    ArrayList<Object> msg_to_remote;
    ArrayList<Object> msg_to_tranmgr;

    /**
     *
     */
    public Action()
    {
        msg_to_remote = new ArrayList<>();
        msg_to_tranmgr = new ArrayList<>();
    }

    /**
     *
     * @param data_to_remote
     * @param data_to_tranmgr
     */
    public Action(byte[] data_to_remote,
            byte[] data_to_tranmgr)
    {
        msg_to_remote = new ArrayList<>();
        msg_to_tranmgr = new ArrayList<>();

        if (data_to_remote != null)
        {
            msg_to_remote.add(data_to_remote);
        }

        if (data_to_tranmgr != null)
        {
            msg_to_tranmgr.add(data_to_tranmgr);
        }
    }

    /**
     *
     * @param data_to_tranmgr
     */
    public void putDataToTranMgr(byte[] data_to_tranmgr)
    {
        if (data_to_tranmgr != null)
        {
            msg_to_tranmgr.add(data_to_tranmgr);
        }
    }

    /**
     *
     * @param data_to_remote
     */
    public void putDataToRemote(byte[] data_to_remote)
    {
        if (data_to_remote != null)
        {
            msg_to_remote.add(data_to_remote);
        }
    }

    /**
     *
     * @return
     */
    public Object getDataToRemote()
    {
        if (msg_to_remote.size() > 0)
        {
            return msg_to_remote.remove(0);
        } else
        {
            return null;
        }
    }

    public int getDataToRemoteSize()
    {
        return msg_to_remote.size();
    }

    public int getDataToTranMgrSize()
    {
        return msg_to_tranmgr.size();
    }

    /**
     *
     * @return
     */
    public Object getDataToTranMgr()
    {
        if (msg_to_tranmgr.size() > 0)
        {
            return msg_to_tranmgr.remove(0);
        } else
        {
            return null;
        }
    }
}
