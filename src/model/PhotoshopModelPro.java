package model;

import model.kernels.IKernel;

public interface PhotoshopModelPro extends PhotoshopModel {

  void kernel(String imageName, String destImageName, IKernel kernel);

  void transform(String imageName, String destImageName, IKernel matrix);


}
