class PostsController < ApplicationController
  protect_from_forgery
  skip_before_filter :verify_authenticity_token, :only => [:create]
  
  def new
    @post = Post.new
  end
  
  def create
    @post = Post.new(post_params)
    
    if params[:post][:device].eql? "Android"
      respond_to do |format|
        format.html # create.html.erb
        format.xml  { render :xml => @post }
        format.json { render :json => @post }
      end
      
      # Save post, should send suitable response code to device on success or failure
      @post.save
    else
      if @post.save
        # If save succeeds, redirect to the home page
        flash[:notice] = "Your book has successfully been posted!"
        redirect_to(:controller => 'bookfinder', :action => 'index') and return
      else
        # If save fails, redisplay the form so user can fix problems
        flash[:notice] = "Your post was unsuccessful."
        render('new') and return
      end
    end
    
    
    
  end
  
  def post_params
    params.require(:post).permit(:title, :edition, :price, :isbn, :cover_type, :device)
  end
end
