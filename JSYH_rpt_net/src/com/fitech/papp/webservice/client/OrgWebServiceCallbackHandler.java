
/**
 * OrgWebServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5.6  Built on : Aug 30, 2011 (10:00:16 CEST)
 */

    package com.fitech.papp.webservice.client;

    /**
     *  OrgWebServiceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class OrgWebServiceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public OrgWebServiceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public OrgWebServiceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for deleteOrg method
            * override this method for handling normal response from deleteOrg operation
            */
           public void receiveResultdeleteOrg(
                    com.fitech.papp.webservice.client.OrgWebServiceStub.DeleteOrgResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from deleteOrg operation
           */
            public void receiveErrordeleteOrg(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for insertOrg method
            * override this method for handling normal response from insertOrg operation
            */
           public void receiveResultinsertOrg(
                    com.fitech.papp.webservice.client.OrgWebServiceStub.InsertOrgResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from insertOrg operation
           */
            public void receiveErrorinsertOrg(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for updateOrg method
            * override this method for handling normal response from updateOrg operation
            */
           public void receiveResultupdateOrg(
                    com.fitech.papp.webservice.client.OrgWebServiceStub.UpdateOrgResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from updateOrg operation
           */
            public void receiveErrorupdateOrg(java.lang.Exception e) {
            }
                


    }
    