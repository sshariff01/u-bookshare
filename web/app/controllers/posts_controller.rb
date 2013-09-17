class PostsController < ApplicationController
  protect_from_forgery
  
  def new
    @post = Post.new
  end
  
  def create
    # @post = Post.new(:title => params[:title], :edition => params[:edition], :price => params[:price], :isbn => params[:isbn], :cover_type => params[:cover_type])
    @post = Post.create(post_params)
    
    if @post.save
      # If save succeeds, redirect to the home page
      flash[:notice] = "Your book has successfully been posted!"
      redirect_to(:controller => 'bookfinder', :action => 'index')
    else
      # If save fails, redisplay the form so user can fix problems
      flash[:notice] = "Your post was unsuccessful."
      render('new')
    end
    
  end
  
  def post_params
    params.require(:post).permit(:title, :edition, :price, :isbn, :cover_type)
  end
end
