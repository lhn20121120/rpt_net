
/**
 * AuthWebServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5.6  Built on : Aug 30, 2011 (10:00:16 CEST)
 */

    package com.fitech.papp.webservice.client;

    /**
     *  AuthWebServiceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class AuthWebServiceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public AuthWebServiceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public AuthWebServiceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for isExist method
            * override this method for handling normal response from isExist operation
            */
           public void receiveResultisExist(
                    com.fitech.papp.webservice.client.AuthWebServiceStub.IsExistResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from isExist operation
           */
            public void receiveErrorisExist(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for setSession method
            * override this method for handling normal response from setSession operation
            */
           public void receiveResultsetSession(
                    com.fitech.papp.webservice.client.AuthWebServiceStub.SetSessionResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from setSession operation
           */
            public void receiveErrorsetSession(java.lang.Exception e) {
            }
                


    }
    