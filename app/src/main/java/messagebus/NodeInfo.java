package messagebus;

import utils.Utility;

/**
 *
 * @author Abhy
 */
public final class NodeInfo
{

    String acquirer_node;
    String issuer_node;
    String super_mid;
    String routing_info;

    /**
     *
     * @param acquirer_node
     * @param issuer_node
     * @param super_mid
     * @param routing_info
     */
    public NodeInfo(String acquirer_node, String issuer_node, String super_mid,
            String routing_info)
    {
        this.acquirer_node = acquirer_node;
        this.issuer_node = issuer_node;
        this.super_mid = super_mid;
        this.routing_info = routing_info;
    }

    /**
     *
     * @param ni
     */
    public NodeInfo(String ni)
    {
        this.acquirer_node = ni.substring(0, 15);
        this.issuer_node = ni.substring(15, 30);
        this.super_mid = ni.substring(30, 45);
        this.routing_info = ni.substring(45, 60);
    }

    /**
     *
     * @return
     */
    public String getAcquirer_node()
    {
        return acquirer_node;
    }

    /**
     *
     * @param acquirer_node
     */
    public void setAcquirer_node(String acquirer_node)
    {
        this.acquirer_node = acquirer_node;
    }

    /**
     *
     * @return
     */
    public String getIssuer_node()
    {
        return issuer_node;
    }

    /**
     *
     * @param issuer_node
     */
    public void setIssuer_node(String issuer_node)
    {
        this.issuer_node = issuer_node;
    }

    /**
     *
     * @return
     */
    public String getRouting_info()
    {
        if (routing_info == null)
        {
            routing_info = "";
        }
        return routing_info;
    }

    /**
     *
     * @param routing_info
     */
    public void setRouting_info(String routing_info)
    {
        this.routing_info = routing_info;
    }

    /**
     *
     * @return
     */
    public String getSuper_mid()
    {
        return super_mid;
    }

    /**
     *
     * @param super_mid
     */
    public void setSuper_mid(String super_mid)
    {
        this.super_mid = super_mid;
    }

    /**
     *
     * @return
     */
    public String getNodeInfo()
    {
        return Utility.resize(acquirer_node.trim(), 15, " ", true)
                + Utility.resize(issuer_node.trim(), 15, " ", true)
                + Utility.resize(super_mid.trim(), 15, " ", true)
                + Utility.resize(routing_info.trim(), 15, " ", true);
    }

}
